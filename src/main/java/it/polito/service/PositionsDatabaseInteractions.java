package it.polito.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.data.Position;
import it.polito.utils.InvalidPositionException;
import it.polito.utils.NullRequestException;
import it.polito.utils.Utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/***
 * Questa classe contiene, per ogni user (nome user usato come chiave nella mappa), la lista di posizioni
 * che egli/ella ha inserito nel database.
 *
 * ATTENZIONE: solamente gli user loggati almeno una volta sono salvati qua dentro.
 *
 * Variabili dentro la classe:
 *      - positionDataBase = mappa thread safe nella quale si salvano le posizioni inviateci da uno specifico utente
 *                          la chiave della mappa è il nome dell'utente;
 *      - ourInstance = variabile statica della classe, quindi utilizzabile da più istanze diverse di servlets
 *
 * IMPORTANTE:
 *       1) getParameter() -> lo uso per prendere i parametri della richiesta
 *       2) session.getAttributes() -> da usare SOLO server-side, in pratica lo uso per passarmi parametri dal filtro al servlet
 *                                  oppure tra diversi servlet
 */
public class PositionsDatabaseInteractions {
    private static ConcurrentHashMap<String, CopyOnWriteArrayList<Position>> positionDataBase = new ConcurrentHashMap<>();
    private static PositionsDatabaseInteractions ourInstance = new PositionsDatabaseInteractions();
    private static double validValueLowerBound = -180;
    private static double validValueUpperBound = 180;
    private static long minTimestamp = 1522000000;

    private PositionsDatabaseInteractions() {
    }

    public static PositionsDatabaseInteractions getInstance() {
        return ourInstance;
    }

    /*
        Due metodi:
         1) newUser ha il compito di aggiungere un nuovo utente --> (caso di login)
         2) deleteUser ha il compito di rimuovere un utente, con relativa lista --> (caso logout)
     */
    public static void newUser(String name) {
        positionDataBase.put(name, new CopyOnWriteArrayList<Position>());
    }

    public static void deleteUser(String name) {
        positionDataBase.remove(name);
    }

    /*
        Funzione che aggiunge una posizione alla lista di posizioni di un utente. Si assume che l'utente qui
        sia correttamente loggato, ma se non presente ancora nella mappa, viene creata una entry per lui.
        Per essere valida, una posizione, deve essere tale che il tratto tra essa e
        quella precedente non sia stato percorso a velocità superiore a 100 m/s.

        ATTENZIONE: assumiamo che i dati siano stati controllati prima di essere passati qua.
     */
    public static void addPosition(String name, Position newPos) throws InvalidPositionException {
        if (!positionDataBase.containsKey(name)) // Nuovo utente
            newUser(name);

        positionDataBase.get(name).add(newPos);
    }

    /*
        Funzione per prendere l'ultima posizione che ci è stata inserita da un certo utente
     */
    protected static Position getLastPositionUser(String name) {
        if (!positionDataBase.containsKey(name)) {
            positionDataBase.put(name, new CopyOnWriteArrayList<>());
            return null;
        }

        int last = positionDataBase.get(name).size();
        if (last == 0)
            return null;

        return positionDataBase.get(name).get(last - 1);
    }

    /*
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req) throws IOException, InvalidPositionException, NullRequestException {
        double speed = 0;

        if (req == null)
            throw (new NullRequestException());
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("user");

        ObjectMapper mapper = new ObjectMapper(); // È la classe magica che ci permatte di leggere il JSON direttamente
        Position postedPosition = mapper.readValue(req.getReader(), Position.class); // BOOM...! Non ho capito come fa a riempirla da sola però :'(

        long maxTimestamp = (System.currentTimeMillis() / 1000L);

        Position lastPos = getLastPositionUser(userName); // Prendo l'ultima posizione inseritaci dallo user

        if (lastPos != null) {
            double distance = lastPos.getDistanceFrom(postedPosition); // Calcolo la distanza tra le due e poi la velocità
            speed = (distance * 1000) / (postedPosition.getTimestamp() - lastPos.getTimestamp()) * 1000; // Perché il timestamp è in millisecondi!!! La formula ritorna i KM
        }

        /**
         * controllo velocità secondo parametri
         * contollo che lon e lat siano entro -180 e 180
         * controllo che il timestamp non sia inferiore al valore di quando abbaimo iniziato il progetto -> passato (si puù anche usare un vaalore più basso
         * controllo che il timestamp non sia maggiore al valore di quando faccio il controlllo -> futuro
         **/
        if (speed > Utilities.MAX_SPEED ||
                Double.compare(postedPosition.getLongitude(), validValueLowerBound) < 0 ||
                Double.compare(postedPosition.getLongitude(), validValueUpperBound) > 0 ||
                Double.compare(postedPosition.getLatitude(), validValueLowerBound) < 0 ||
                Double.compare(postedPosition.getLatitude(), validValueUpperBound) > 0 ||
                Long.compare(postedPosition.getTimestamp(), minTimestamp) < 0 ||
                Long.compare(postedPosition.getTimestamp(), maxTimestamp) > 0) {
            // Non vengono soddisfatti i requisiti quindi tiro un'exception
            throw new InvalidPositionException();
        } else {
            // Ora provo ad aggiungerla
            addPosition(userName, postedPosition);
        }
    }

    /*
        Qua è dove accade la magia della GET invece. Il motivo per cui esiste è lo stesso di prima. Qua ho deciso di considerare alcuni casi
        speciali:
            - se c'è un parametro before ritorno la lista di solamente le posizioni prima di un certo timestamp
            - se non esiste quel parametro ritorno la lista completa
     */
    public static void performGet(HttpServletRequest req, HttpServletResponse resp) throws NullRequestException, IOException {
        if (req == null)
            throw (new NullRequestException());

        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("user");
        String beforeAsString = (String) req.getParameter("before");
        String afterAsString = (String) req.getParameter("after");
        long before = Long.MAX_VALUE;
        long after = -1;

        if (beforeAsString != null)
            before = Long.parseLong(beforeAsString); // Prendo un eventuale timestamp nella richiesta
        if (afterAsString != null)
            after = Long.parseLong(afterAsString); // Prendo un eventuale timestamp limite

        List<Position> positionsList = retrievePositions(userName, before, after); // Prendo la lista delle position richieste

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), positionsList);
    }

    private static List<Position> retrievePositions(String name, long before, long after) {
        if (before == 0) {
            // Vuole ritornate tutte quante le posizioni
            return new ArrayList<>(positionDataBase.get(name));
        } else {
            // C'è da controllare il timestamp
            return positionDataBase.get(name).stream()
                    .filter(
                            p -> (p.getTimestamp() < before && p.getTimestamp() > after)
                    )
                    .distinct()
                    .collect(Collectors.toList());
        }
    }
}

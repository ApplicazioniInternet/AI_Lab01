package it.polito.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpRequest;
import it.polito.data.Position;
import it.polito.utils.InvalidSpeedException;
import it.polito.utils.NullRequestException;
import it.polito.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
public class PositionsDatabase {
    private static ConcurrentHashMap<String, CopyOnWriteArrayList<Position>> positionDataBase = null;
    private static PositionsDatabase ourInstance = new PositionsDatabase();

    public static PositionsDatabase getInstance() {
        return ourInstance;
    }

    private PositionsDatabase() {
        positionDataBase = new ConcurrentHashMap<String, CopyOnWriteArrayList<Position>>();
    }

    /*
        Due metodi:
         1) newUser ha il compito di aggiungere un nuovo utente --> (caso di login)
         2) deleteUser ha il compito di rimuovere un utente, con relativa lista --> (caso logout)
     */
    public static void newUser(String name){
        positionDataBase.put(name, new CopyOnWriteArrayList<Position>());
    }

    public static void deleteUser(String name){
        positionDataBase.remove(name);
    }

    /*
        Funzione che aggiunge una posizione alla lista di posizioni di un utente. Si assume che l'utente qui
        sia correttamente loggato, ma se non presente ancora nella mappa, viene creata una entry per lui.
        Per essere valida, una posizione, deve essere tale che il tratto tra essa e
        quella precedente non sia stato percorso a velocità superiore a 100 m/s.

        ATTENZIONE: assumiamo che i dati siano stati controllati prima di essere passati qua.
     */
    public static void addPosition(String name, Position newPos) throws InvalidSpeedException {
        if(!positionDataBase.containsKey(name)) // Nuovo utente
            newUser(name);

        Position lastPos = getLastPositionUser(name); // Prendo l'ultima posizione inseritaci dallo user
        double distance = lastPos.getDistanceFrom(newPos); // Calcolo la distanza tra le due e poi la velocità
        double speed = (distance*1000)/(newPos.getTime() - lastPos.getTime())*1000; // Perché il timestamp è in millisecondi!!! La formula ritorna i KM

        if(speed > Utils.MAX_SPEED)
            // Non vengono soddisfatti i requisiti quindi tiro un'exception
            throw( new InvalidSpeedException() );
        else
            // Tutto ok, posso aggiungere la posizione
            positionDataBase.get(name).add(newPos);
    }

    /*
        Funzione per prendere l'ultima posizione che ci è stata inserita da un certo utente
     */
    private static Position getLastPositionUser(String name) {
        int last = positionDataBase.get(name).size();
        return positionDataBase.get(name).get(last);
    }

    /*
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req) throws IOException, InvalidSpeedException, NullRequestException {
        if(req == null)
            throw( new NullRequestException() );

        String userName = (String) req.getParameter("user");

        ObjectMapper mapper = new ObjectMapper(); // È la classe magica che ci permatte di leggere il JSON direttamente
        Position postedPosition = mapper.readValue(req.getReader(), Position.class); // BOOM...! Non ho capito come fa a riempirla da sola però :'(

        // Ora provo ad aggiungerla
        addPosition(userName, postedPosition);
    }

    /*
        Qua è dove accade la magia della GET invece. Il motivo per cui esiste è lo stesso di prima. Qua ho deciso di considerare alcuni casi
        speciali:
            - se c'è un parametro before ritorno la lista di solamente le posizioni prima di un certo timestamp
            - se non esiste quel parametro ritorno la lista completa
     */
    public static void performGet(HttpServletRequest req, HttpServletResponse resp) throws NullRequestException, IOException {
        if(req == null)
            throw( new NullRequestException() );

        String userName = (String) req.getParameter("user"); // Prendo lo username
        String beforeAsString = (String) req.getParameter("before");
        long before = 0;

        if(beforeAsString != null)
            before = Long.parseLong(beforeAsString); // Prendo un eventuale timestamp nella richiesta

        List<Position> positionsList = retrievePositions(userName, before); // Prendo la lista delle position richieste
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), positionsList);
    }

    private static List<Position> retrievePositions(String name, long before) {
        if(before == 0){
            // Vuole ritornate tutte quante le posizioni
            return new ArrayList<>(positionDataBase.get(name));
        } else {
            // C'è da controllare il timestamp
            return positionDataBase.get(name).stream()
                    .filter(p -> p.getTime() < before)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }
}

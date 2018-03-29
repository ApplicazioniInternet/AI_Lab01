package it.polito.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.data.User;
import it.polito.data.UserValue;
import it.polito.utils.NullRequestException;
import it.polito.utils.UnauthorizedException;
import it.polito.utils.Utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * Questa classe contiene tutto ciò che è stato letto da file. Verrà utilizzata per potere autenticare
 * gli user (in particolare verrà utilizzata dal servlet di login).
 */
public class UsersDatabaseInteractions {
    private static ConcurrentHashMap<String, User> users = null;
    private static UsersDatabaseInteractions ourInstance = new UsersDatabaseInteractions();

    /*
            Questa è il costruttore che sarà responsabile di leggere il file degli users
            e caricarne il contenuto in una mappa che è THREAD SAFE, siccome sarà possibile accedervi
            da diversi servlet (potenzialmente).
         */
    private UsersDatabaseInteractions() {
        users = new ConcurrentHashMap<String, User>();
    }

    public static UsersDatabaseInteractions getInstance() {
        return ourInstance;
    }

    /*
        Funzione che ci controlla se le credenziali inserite da uno specifico user
        sono corrette.
     */
    public static boolean isAuthorized(String name, String pwd) {
        return (users.containsKey(name)) && (users.get(name).isPWdOk(pwd));
    }

    /*
        Funzione che ritorna uno user dato il suo nome.
     */
    public static User getUser(String name) {
        return users.get(name);
    }

    /*
        Funzione che mi permette di aggiungere un nuovo user (quindi potenzialmente
        la posso sfruttare poi per implementare anche un meccanismo di registrazione).
     */
    public static void putUser(String name, String pwd) {
        UserValue uv = new UserValue();
        uv.setUsername(name);
        uv.setPassword(pwd);
        users.put(name, new User(uv));
    }

    /*
        Analogamente alla funzione in PositionsDatabaseInteractions
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req, ServletContext sc) throws IOException, NullRequestException, UnauthorizedException {
        if (req == null)
            throw ( new NullRequestException() );

        if(users.isEmpty())
            loadUsers(Utils.FILE_PATH, sc);

        ObjectMapper mapper = new ObjectMapper(); // È la classe magica che ci permatte di leggere il JSON direttamente
        UserValue postedUser = mapper.readValue(req.getReader(), UserValue.class);
        /*System.out.println("JSON -> JAVA: 1) username: " + postedUser.getUsername() + "\n"
                                        + "2) password: " + postedUser.getPassword()
                            );*/

        String pwd = postedUser.getPassword();
        /*
            Qui elaborazione pwd: hash (direi SHA-256) + XOR con Username
            Dubbio: è corretto memorizzare nella mappa password come stringa o è meglio come byte?
         */

        //Questa eccezione dovrebbe ritornare automaticamente al client un codice 401 unauthorize
        if (!isAuthorized(postedUser.getUsername(), pwd))
            throw new UnauthorizedException();
        else {
            //Devo settare l'attributo "user" nella sessione
            System.out.println("L'utente <<" + postedUser.getUsername() + ">> è autorizzato!");
            HttpSession session = req.getSession();
            session.setAttribute("user", postedUser.getUsername());
        }
    }

    /*
        Funzione che effettivamente carica gli users nella struttura dati.
     */
    private static void loadUsers(String file, ServletContext sc) throws IOException {
        if(sc == null)
            throw( new IOException() );

        InputStream is = sc.getResourceAsStream(file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferedReader =
                new BufferedReader(isr);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println("Ho letto: " + line);
            String[] res = line.split(" ");
            putUser(res[0], res[1]);
        }

        bufferedReader.close();
    }

    /*
        Solo per debuggare
     */
    public static void performGet(ServletContext sc) throws IOException {
        loadUsers(Utils.FILE_PATH, sc);
    }
}

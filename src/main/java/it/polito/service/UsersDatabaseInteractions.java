package it.polito.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.data.User;
import it.polito.utils.NullRequestException;
import it.polito.utils.UnauthorizedException;
import it.polito.utils.Utils;

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

    public static UsersDatabaseInteractions getInstance() {
        return ourInstance;
    }

    /*
        Questa è il costruttore che sarà responsabile di leggere il file degli users
        e caricarne il contenuto in una mappa che è THREAD SAFE, siccome sarà possibile accedervi
        da diversi servlet (potenzialmente).
     */
    private UsersDatabaseInteractions() {
        users = new ConcurrentHashMap<String, User>();
        try{
            loadUsers(Utils.FILE_PATH);
        } catch (FileNotFoundException e) {
            throw( new RuntimeException(e) );
        } catch (IOException e) {
            throw( new RuntimeException(e) );
        }
    }

    /*
        Funzione che effettivamente carica gli users nella struttura dati.
     */
    private void loadUsers(String file) throws IOException {
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader =
                new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] res = line.split(" ");
            putUser(res[0], res[1]);
        }

        bufferedReader.close();
    }

    /*
        Funzione che ci controlla se le credenziali inserite da uno specifico user
        sono corrette.
     */
    public static boolean isAuthorized(String name, String pwd){
        return (users.containsKey(name)) && (users.get(name).isPWdOk(pwd));
    }

    /*
        Funzione che ritorna uno user dato il suo nome.
     */
    public static User getUser(String name){
        return users.get(name);
    }

    /*
        Funzione che mi permette di aggiungere un nuovo user (quindi potenzialmente
        la posso sfruttare poi per implementare anche un meccanismo di registrazione).
     */
    public static void putUser(String name, String pwd){
        users.put(name, new User(name, pwd));
    }

    /*
        Analogamente alla funzione in PositionsDatabaseInteractions
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req) throws IOException, NullRequestException, UnauthorizedException {
        if(req == null)
            throw( new NullRequestException() );

        ObjectMapper mapper = new ObjectMapper(); // È la classe magica che ci permatte di leggere il JSON direttamente
        User postedUser = mapper.readValue(req.getReader(), User.class);

        String pwd = postedUser.getPassword();
        /*
            Qui elaborazione pwd: hash (direi SHA-256) + XOR con Username
            Dubbio: è corretto memorizzare nella mappa password come stringa o è meglio come byte?
         */

        //Questa eccezione dovrebbe ritornare automaticamente al client un codice 401 unauthorize
        if(!isAuthorized(postedUser.getUsername(), pwd))
            throw new UnauthorizedException();
        else{
            //Devo settare l'attributo "user" nella sessione
            HttpSession session = req.getSession();
            session.setAttribute("user", postedUser.getUsername());
        }
    }
}

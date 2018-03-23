package it.polito.service;

import it.polito.data.User;
import it.polito.utils.Utils;

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
}

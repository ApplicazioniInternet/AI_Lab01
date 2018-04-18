package it.polito.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.data.User;
import it.polito.drivers.SQLUserDAO;
import it.polito.utils.NullRequestException;
import it.polito.utils.UnauthorizedException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

/***
 * Questa classe contiene tutto ciò che è stato letto da file. Verrà utilizzata per potere autenticare
 * gli user (in particolare verrà utilizzata dal servlet di login).
 */
public class UsersDatabaseInteractions {
    private static UsersDatabaseInteractions ourInstance = new UsersDatabaseInteractions();

    public static UsersDatabaseInteractions getInstance() {
        return ourInstance;
    }

    /*
        Funzione che ritorna uno user dato il suo nome.
     */
//    public static User getUser(String name) {
//
//        todo select user from db
//    }

    /*
        Funzione che mi permette di aggiungere un nuovo user (quindi potenzialmente
        la posso sfruttare poi per implementare anche un meccanismo di registrazione).
     */
    public static void putUser(String name, String pwd) {
        User uv = new User(name, pwd, false);
        //todo write sqlquery to insert user
    }

    /*
        Analogamente alla funzione in PositionsDatabaseInteractions
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req, ServletContext sc) throws IOException, NullRequestException, UnauthorizedException {
        if (req == null)
            throw (new NullRequestException());

        SQLUserDAO DBuser = new SQLUserDAO();
        ObjectMapper mapper = new ObjectMapper();
        User requestUser = mapper.readValue(req.getReader(), User.class);

        //Questa eccezione dovrebbe ritornare automaticamente al client un codice 401 unauthorize
        if (!DBuser.login(requestUser))
            throw new UnauthorizedException();
        else {
            //Devo settare l'attributo "user" nella sessione
            HttpSession session = req.getSession();
            //System.out.println("Lo user <<" + postedUser.getUsername() + ">> è autorizzato!");
            session.setAttribute("user", requestUser.getUsername());
        }
    }

}

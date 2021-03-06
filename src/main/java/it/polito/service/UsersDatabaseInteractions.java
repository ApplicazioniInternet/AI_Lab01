package it.polito.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.data.User;
import it.polito.data.UserInfo;
import it.polito.drivers.PostgressUserDAO;
import it.polito.utils.NullRequestException;
import it.polito.utils.UnauthorizedException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static jdk.internal.jline.internal.Log.debug;

/***
 * Questa classe contiene tutto ciò che è stato letto da file. Verrà utilizzata per potere autenticare
 * gli user (in particolare verrà utilizzata dal servlet di login).
 */
public class UsersDatabaseInteractions {
    private static UsersDatabaseInteractions ourInstance = new UsersDatabaseInteractions();
    private static final String TAG = "[UsersDatabaseInteractions] ";
    public static UsersDatabaseInteractions getInstance() {
        return ourInstance;
    }

    /*
        Analogamente alla funzione in PositionsDatabaseInteractions
        Questa funzione è dove accade la vera magia della POST. Per mantenere il design pattern corretto,
        il servlet non si deve occupare dell'allocazione di oggetti e cazzate varie, ci pensa questa funzione che
        ha coscienza della struttura dei dati (data layer).
     */
    public static void performPost(HttpServletRequest req, ServletContext sc) throws IOException,
            NullRequestException, UnauthorizedException {

        if (req == null) throw (new NullRequestException());

        PostgressUserDAO DBuser = new PostgressUserDAO();

        ObjectMapper mapper = new ObjectMapper();
        UserInfo requestUserInfo = mapper.readValue(req.getReader(), UserInfo.class);

        sc.log(TAG + requestUserInfo.toString());

        User requestUser = User.newUser(requestUserInfo);

        sc.log(TAG + requestUser.toString());

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

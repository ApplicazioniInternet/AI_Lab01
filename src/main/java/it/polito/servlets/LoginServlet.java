package it.polito.servlets;

import it.polito.service.UsersDatabaseInteractions;
import it.polito.utils.NullRequestException;
import it.polito.utils.UnauthorizedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * Web Servlet per fare il login di uno user. Deve controllare che lo user esista nel database
 * e che le sue credenziali siano corrette.
 * Se autenticato correttamente lo user verrà rediretto verso il filtro con un parametro della session
 * settato (parametro "user").
 */

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet{
    private static final UsersDatabaseInteractions dbUser = UsersDatabaseInteractions.getInstance();

    /*
        201 -> tutto ok, il tizio era uno a posto e allora la sua sessione è stata creata e settata
        401 -> utente non autorizzato
        400 -> qualcosa non andava bene nel formato della richiesta
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            dbUser.performPost(req, getServletContext());

            // Se sono qui significa che l'utente è stato autenticato con successo
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch(UnauthorizedException e){
            /*
                Questa eccezione dovrebbe ritornare in automatico al client una risposta con codice 401 Unauthorize
                Poichè fornisce una risposta al client penso sia meglio lanciarla nel servlet, mentre nel service si
                utilizza un'ecceziuone custom (UnauthorizeException)
             */
            //throw new NotAuthorizedException(req);
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        catch(IOException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NullRequestException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}

package it.polito.servlets;

import it.polito.service.UsersDatabaseInteractions;
import it.polito.utils.UnauthorizedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.io.PrintWriter;

/***
 * Web Servlet per fare il login di uno user. Deve controllare che lo user esista nel database
 * e che le sue credenziali siano corrette.
 * Se autenticato correttamente lo user verrà rediretto verso il filtro con un parametro della session
 * settato (parametro "user").
 */

@WebServlet(urlPatterns = "/positions/login")
public class LoginServlet extends HttpServlet{
    private static final UsersDatabaseInteractions dbUser = UsersDatabaseInteractions.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            dbUser.performPost(req);
            //Se sono qui significa che l'utente è stato autenticato con successo
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch(UnauthorizedException e){
            /*
                Questa eccezione dovrebbe ritornare in automatico al client una risposta con codice 401 Unauthorize
                Poichè fornisce una risposta al client penso sia meglio lanciarla nel servlet, mentre nelk service si
                utilizza un'ecceziuone custom (UnauthorizeException)
             */
            throw new NotAuthorizedException(req);
        }
        catch(Exception e){
            //Teoricamente sia un'IOException sia una NullRequest Exception sono dovute ad una BadRequest
            throw new BadRequestException();
        }
    }
}

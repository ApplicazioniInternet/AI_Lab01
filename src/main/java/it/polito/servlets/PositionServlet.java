package it.polito.servlets;

import it.polito.service.PositionsDatabaseInteractions;
import it.polito.utils.InvalidPositionException;
import it.polito.utils.NullRequestException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * Web Servlet per aggiungere una posizione oppure prendere la lista delle posizioni inserite dallo
 * user.
 */
@WebServlet(urlPatterns = "/")
public class PositionServlet extends HttpServlet{
    private final PositionsDatabaseInteractions db = PositionsDatabaseInteractions.getInstance();

    /*
        200 -> tutto ok
        204 -> la richiesta aveva null content
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            db.performGet(req, resp);

            // Se arrivo qua vuol dire che è tutto ok e che il mio body è stato creato correttamente
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NullRequestException e) {
            // Dico al client che mi ha inviato qualcosa senza content (penso che in teoria si riferisca al content type,
            // ma ci capiamo tra di noi
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            //throw ( new ServletException(e) );
        }
    }

    /*
        201 -> tutto ok, la position è stata creata
        400 -> la velocità non andava bene
        204 -> richiesta con null content
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            try {
                db.performPost(req);

                // Se arrivo qua allora vuol dire che tutto va bene e la position è stata creata
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (InvalidPositionException e) {
                // Dico al client che la richiesta aveva un formato sbagliato,
                // la velocità lat lon o timestamp non vanno bene
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                //throw( new ServletException() );
            } catch (NullRequestException e) {
                // Dico al client che mi ha inviato qualcosa senza content (penso che in teoria si riferisca al content type,
                // ma ci capiamo tra di noi
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                //throw ( new ServletException(e) );
            }
    }
}

package it.polito.servlets;

import it.polito.service.PositionsDatabase;
import it.polito.utils.InvalidSpeedException;
import it.polito.utils.NullRequestException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * Web Servlet per aggiungere una posizione oppure prendere la lista delle posizioni inserite dallo
 * user.
 */
@WebServlet(urlPatterns = "/positions")
public class PositionServlet extends HttpServlet{
    static final PositionsDatabase db = PositionsDatabase.getInstance();

    /*
        200 -> tutto ok
        altri codici -> qualcosa è andato male (da fare nelle eccezioni!)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            db.performGet(req, resp);

            // Se arrivo qua vuol dire che è tutto ok e che il mio body è stato creato correttamente
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NullRequestException e) {
            // ****** DA METTERE CODICE DI ERRORE ******
            throw ( new ServletException(e) );
        }
    }

    /*
        201 -> tutto ok, la position è stata creata
        altri codici -> qualcosa è andato male (da fare nelle eccezioni!)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                db.performPost(req);

                // Se arrivo qua allora vuol dire che tutto va bene e la position è stata creata
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (InvalidSpeedException e) {
                // ****** DA METTERE CODICE DI ERRORE ******
                throw( new ServletException() );
            } catch (NullRequestException e) {
                // ****** DA METTERE CODICE DI ERRORE ******
                throw ( new ServletException(e) );
            }
    }
}

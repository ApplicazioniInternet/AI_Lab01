package it.polito.servlets;

import it.polito.service.UsersDatabaseInteractions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Utilizzando il remtodo req.isSecure il risultato è true anche se testando la richiesta non è su https
        //Qui l'utente dovrebbe postare username e password e poi si invoca db.isAuthorized(usr, pwd)
        final UsersDatabaseInteractions db = UsersDatabaseInteractions.getInstance();
    }
}

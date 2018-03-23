package it.polito.servlets;

import javax.servlet.http.HttpServlet;

/***
 * Web Servlet per fare il login di uno user. Deve controllare che lo user esista nel database
 * e che le sue credenziali siano corrette.
 * Se autenticato correttamente lo user verrà rediretto verso il filto con un parametro della session
 * settato (parametro "user").
 */
public class LoginServlet extends HttpServlet{
}

package it.polito.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Filtro che fa un po' di tutto. Principalmente controlla che lo user sia già stato autenticato, prima di potere
 * accedere alla risorsa "/positions", se non è loggato lo redirige verso il servlet per il login.
 * Controlla anche che il tipo di body inviato (se loggato l'utente e se sta facendo una post) sia di tipo "application/json".
 */
public class GeneralPurposeFilter implements Filter{
    private ServletContext context;

    /*
        Funzione di inizializzazione, in pratica non fa nulla.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(filterConfig.getServletContext() == null)
            throw ( new ServletException() );
        else
            this.context = filterConfig.getServletContext();
    }

    /*
        Funzione che fa gli effettivi controlli del filtro
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse resp;

        if(servletRequest instanceof HttpServletRequest){
            // La richiesta e il response possono essere castate.
            req = (HttpServletRequest) servletRequest;
            resp = (HttpServletResponse) servletResponse;
            HttpSession session = req.getSession();

            if(session.getAttribute("user") == null && !session.getAttribute("logout").equals("true")){
                // L'utente non si è ancora loggato, quindi devo redirigerlo al servlet di login
                String newUrl = req.getRequestURI() + "/login";
                resp.sendRedirect(newUrl);
            }else{
                // L'utente è autenticato controllo se si vuole sloggare e in caso lo redirigo al servlet per il logout
                if(session.getAttribute("logout").equals("true")){
                    String newUrl = req.getRequestURI() + "/logout";
                    resp.sendRedirect(newUrl);
                }else{
                    // L'utente è autenticato e vuole fare delle operazioni. Controllo il contentType e che tipo di richiesta è.
                    if(!(req.getContentType().equals("application/json") && req.getMethod().equals("POST"))){
                        // Non posso capire il messaggio, quindi chiudo qua la comunicazione
                        resp.reset();
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }
            }
        }
    }

    /*
        Funzione che non fa nulla, in teoria se avessimo avuto della roba allocata dinamicamente (tipo un client che
        è stato aperto per parlare con Neo4j) dovremmo chiuderlo qua.
     */
    @Override
    public void destroy() {

    }
}

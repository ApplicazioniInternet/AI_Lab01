package it.polito.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro che fa un po' di tutto. Principalmente controlla che lo user sia già stato autenticato, prima di potere
 * accedere alla risorsa "/positions", se non è loggato lo redirige verso il servlet per il login.
 * Controlla anche che il tipo di body inviato (se loggato l'utente e se sta facendo una post) sia di tipo "application/json".
 */
public class GeneralPurposeFilter implements Filter{
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(filterConfig.getServletContext() == null)
            throw ( new ServletException() );
        else
            this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse resp;

        if(servletRequest instanceof HttpServletRequest){
            req = (HttpServletRequest) servletRequest;
            HttpSession session = req.getSession();

            if(session.getAttribute("user") == null){
                // L'utente non si è ancora loggato, quindi devo redirigerlo al servlet di login
                resp = (HttpServletResponse) servletResponse;
                resp.sendRedirect("/login");
            }
        }
    }

    @Override
    public void destroy() {

    }
}

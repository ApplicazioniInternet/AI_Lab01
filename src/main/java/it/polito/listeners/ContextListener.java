package it.polito.listeners;

import it.polito.drivers.DBInterface;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();

        try {
            Connection c = DBInterface.getDBInterface();
            ctx.setAttribute("DBConnection", c);
            System.out.println("Database connection initialized for Application.");
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        try {
            Connection c = (Connection) ctx.getAttribute("DBConnection");
            c.close();
            System.out.println("Database connection closed for Application.");
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

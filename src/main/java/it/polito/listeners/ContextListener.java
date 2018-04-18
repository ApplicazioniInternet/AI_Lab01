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
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Database connection initialized for Application.");
        }catch (ClassNotFoundException e){
            throw new
                    ExceptionInInitializerError(e);
            //o invocare System.exit()
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

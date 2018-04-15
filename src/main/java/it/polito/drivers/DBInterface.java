package it.polito.drivers;
import it.polito.utils.Utilities;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

// todo use application Listener on startup to get this connection,
// then use 2 DAO to get and update users and positions
public class DBInterface {
    private static Connection conn = null;

    public static Connection getDBInterface() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection
                    (Utilities.urlDB,Utilities.DBuser,Utilities.DBpwd);
        }

        return conn;
    }

}

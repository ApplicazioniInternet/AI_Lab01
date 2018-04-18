package it.polito.drivers;
import it.polito.utils.Utilities;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBInterface {

    public static Connection getConnectionDB() throws SQLException {
        Connection conn = DriverManager.getConnection
                    (Utilities.urlDB,Utilities.DBuser,Utilities.DBpwd);

        return conn;
    }

}

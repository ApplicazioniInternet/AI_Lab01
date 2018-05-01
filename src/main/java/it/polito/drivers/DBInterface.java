package it.polito.drivers;
import it.polito.utils.Constants;
import it.polito.utils.Utilities;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBInterface {

    public static Connection getConnectionDB() throws SQLException {
        Connection conn = DriverManager.getConnection
                    (Constants.urlDB, Constants.DBuser, Constants.DBpwd);

        return conn;
    }

}

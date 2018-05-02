package it.polito.drivers;
import it.polito.utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBInterface {

    public static Connection getConnectionDB() throws SQLException {
        Connection conn = DriverManager.getConnection
                (Constants.urlDB, Constants.DBuser, Constants.DBpwd);

        return conn;
    }
}

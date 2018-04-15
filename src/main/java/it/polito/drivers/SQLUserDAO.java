package it.polito.drivers;

import it.polito.data.User;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDAO implements UserDAO {

    public boolean login(Connection c, User u) {
        Statement s = null;
        ResultSet rs = null;
        boolean result = false;
        String pwd;
        try {
            s = c.createStatement();
            rs = s.executeQuery("SELECT password FROM Contatti WHERE user = " + u.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // assumiamo che username sia unico e la password sia già salvata criptata
                if (rs.next()) {
                    pwd = rs.getString(1);
                    if (pwd.compareTo(new String(u.getDigestPassword(), StandardCharsets.UTF_8)) == 0)
                        result = true;
                }
                if(rs!=null)
                    rs.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(s!=null)
                    s.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(c!=null)
                    c.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}


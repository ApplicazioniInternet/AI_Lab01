package it.polito.drivers;

import it.polito.data.User;

import javax.servlet.ServletContext;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class PostgressUserDAO implements UserDAO {

    public boolean login(User u) {
        Connection c = null;
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder();
        ResultSet rs = null;
        boolean result = false;
        String pwd;

        query.append("SELECT hashedPassword FROM User WHERE userID = ?");

        try {
            c = DBInterface.getConnectionDB();
            ps = c.prepareStatement(query.toString());
            ps.setString(1, u.getUsername());

            System.out.println(query);

            rs = ps.executeQuery();
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
                if(rs != null)
                    rs.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(ps != null)
                    ps.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(c != null)
                    c.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}

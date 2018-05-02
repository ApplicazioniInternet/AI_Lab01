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

        query.append("SELECT \"hashedPassword\" FROM \"User\" WHERE \"userID\" = ?");

        try {
            c = DBInterface.getConnectionDB();
            ps = c.prepareStatement(query.toString());
            ps.setString(1,  u.getUsername());

            System.err.println(ps.toString());

            rs = ps.executeQuery();

            if (rs.next()) {
                pwd = rs.getString("hashedPassword");
                if (u.isPwdOk(pwd))
                    result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(rs != null)
                    rs.close();
            } catch(SQLException e) { }
            try {
                if(ps != null)
                    ps.close();
            } catch(SQLException e) { }
            try {
                if(c != null)
                    c.close();
            } catch(SQLException e) { }
        }
        return result;
    }
}

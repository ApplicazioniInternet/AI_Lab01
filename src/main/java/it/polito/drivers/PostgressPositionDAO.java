package it.polito.drivers;

import it.polito.data.Position;
import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgressPositionDAO implements PositionDAO {

    @Override
    public void insert(String username, Position p) {
        Connection c = null;
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO Position ")
                .append("(userID, pos, timestamp) ")
                .append("VALUES (?, (?,?), ?)");

        try {
            c = DBInterface.getConnectionDB();
            ps = c.prepareStatement(query.toString());
            //Utilizzando lo username come primary key, se ne deve garantire l'univocità
            ps.setString(1, username);
            ps.setDouble(2, p.getLatitude());
            ps.setDouble(3, p.getLongitude());
            ps.setLong(4, p.getTimestamp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                    throw new RuntimeException(e);
            }
            try {
                if (c != null)
                    c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Position> getLastPosition(String username) {
        Connection c = null;
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position W WHERE userID = ? ORDER BY timestamp DESC LIMIT 1");

        try {
            c = DBInterface.getConnectionDB();
            ps = c.prepareStatement(query.toString());
            ps.setString(1, username);
            readAndProcessData(ps.executeQuery(), result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                    throw new RuntimeException(e);
            }
            try {
                if (c != null)
                    c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<Position> findByTimestamp(String username, long before, long after) {
        Connection c = null;
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position ")
                .append("WHERE userID = ? AND timestamp < ? AND timestamp > ?");

        try {
            if (username != null) {
                c = DBInterface.getConnectionDB();
                ps = c.prepareStatement(query.toString());
                ps.setString(1, username);
                ps.setLong(2, before);
                ps.setLong(3, after);
                readAndProcessData(ps.executeQuery(), result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                    throw new RuntimeException(e);
            }
            try {
                if (c != null)
                    c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private void readAndProcessData(ResultSet rs, List<Position> l) {
        try {
            //Leggo ogni riga ritornata, creo la Position e la aggiungo alla lista
            while (rs.next()) {
                PGpoint point = (PGpoint) rs.getObject("pos");
                long timestamp = rs.getLong("timestamp");

                Position p = new Position();
                p.setLatitude(point.x);
                p.setLongitude(point.y);
                p.setTimestamp(timestamp);

                l.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

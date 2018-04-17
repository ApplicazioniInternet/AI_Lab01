package it.polito.drivers;

import it.polito.data.Position;
import org.postgresql.geometric.PGpoint;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostgressPositionDAO implements PositionDAO {

    @Override
    public void insert(String username, Position p) {
        PreparedStatement ps = null;
        //Prepared statement per evitare SQL injection
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO Position ")
                .append("(userID, pos, timestamp) ")
                .append("VALUES (?, point(?,?), ?)");

        try {
            ps = DBInterface.getDBInterface().prepareStatement(query.toString());
            //Utilizzando lo username come primary key, se ne deve garantire l'univocità
            ps.setString(1, username);
            ps.setDouble(2, p.getLatitude());
            ps.setDouble(3, p.getLongitude());
            ps.setTimestamp(4, new Timestamp(p.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            //Più che rilanciare l'eccezione non saprei cosa fare
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                //Più che rilanciare l'eccezione non saprei cosa fare
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Position> findAll(String username) {
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position ");

        try {
            if (username != null) {
                query.append("WHERE userID = ?");
                ps = DBInterface.getDBInterface().prepareStatement(query.toString());
                ps.setString(1, username);
            } else
                ps = DBInterface.getDBInterface().prepareStatement(query.toString());

            readAndProcessData(ps.executeQuery(), result);
        } catch (SQLException e) {
            //Più che rilanciare l'eccezione non saprei cosa fare
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                //Più che rilanciare l'eccezione non saprei cosa fare
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<Position> findByBefore(String username, long before) {
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position ")
                .append("WHERE userID = ? AND timestamp < ?");

        try {
            if (username != null) {
                ps = DBInterface.getDBInterface().prepareStatement(query.toString());
                ps.setString(1, username);
                ps.setTimestamp(2, new Timestamp(before));
                readAndProcessData(ps.executeQuery(), result);
            }
        } catch (SQLException e) {
            //Più che rilanciare l'eccezione non saprei cosa fare
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                //Più che rilanciare l'eccezione non saprei cosa fare
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<Position> findByAfter(String username, long after) {
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position ")
                .append("WHERE userID = ? AND timestamp > ?");

        try {
            if (username != null) {
                ps = DBInterface.getDBInterface().prepareStatement(query.toString());
                ps.setString(1, username);
                ps.setTimestamp(2, new Timestamp(after));
                readAndProcessData(ps.executeQuery(), result);
            }
        } catch (SQLException e) {
            //Più che rilanciare l'eccezione non saprei cosa fare
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                //Più che rilanciare l'eccezione non saprei cosa fare
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<Position> findByTimestamp(String username, long before, long after) {
        PreparedStatement ps = null;
        List<Position> result = new ArrayList<Position>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Position ")
                .append("WHERE userID = ? AND timestamp < ? AND timestamp > ?");

        try {
            if (username != null) {
                ps = DBInterface.getDBInterface().prepareStatement(query.toString());
                ps.setString(1, username);
                ps.setTimestamp(2, new Timestamp(before));
                ps.setTimestamp(3, new Timestamp(after));
                readAndProcessData(ps.executeQuery(), result);
            }
        } catch (SQLException e) {
            //Più che rilanciare l'eccezione non saprei cosa fare
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                //Più che rilanciare l'eccezione non saprei cosa fare
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
                long timestamp = rs.getTimestamp("timestamp").getTime();

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

package it.polito.drivers;

import it.polito.data.Position;

import java.util.List;

public interface PositionDAO {
    public void insert(String username, Position p);

    public List<Position> findByTimestamp(String username, long before, long after);

    public List<Position> getLastPosition(String username);
}

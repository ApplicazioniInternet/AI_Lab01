package it.polito.drivers;

import it.polito.data.Position;

import java.util.List;

public interface PositionDAO {
    public void insert(String username, Position p);

    //Se username è nul ritorna tutte le Position nel DB
    public List<Position> findAll(String username);

    public List<Position> findByBefore(String username, long before);

    public List<Position> findByAfter(String username, long after);

    public List<Position> findByTimestamp(String username, long before, long after);
}

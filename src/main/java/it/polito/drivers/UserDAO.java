package it.polito.drivers;

import it.polito.data.User;

import java.sql.Connection;

public interface UserDAO {
    public boolean login(Connection c, User u);
//    public void insert(User u);
//    public void delete(User u);
//    public void update(User u);
}

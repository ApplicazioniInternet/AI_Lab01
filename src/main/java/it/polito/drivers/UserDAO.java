package it.polito.drivers;

import it.polito.data.User;

import java.sql.Connection;

public interface UserDAO {
    boolean login(User u);
}

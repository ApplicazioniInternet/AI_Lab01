package it.polito.service;

import it.polito.data.User;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsersDatabase {
    private static ConcurrentHashMap<String, User> users = null;
    private final String FILE_PATH = "users.txt";

    private static UsersDatabase ourInstance = new UsersDatabase();

    public static UsersDatabase getInstance() {
        return ourInstance;
    }

    private UsersDatabase() {
        users = new ConcurrentHashMap<String, User>();
        try{
            loadUsers(FILE_PATH);
        } catch (FileNotFoundException e) {
            throw( new RuntimeException(e) );
        } catch (IOException e) {
            throw( new RuntimeException(e) );
        }
    }

    private void loadUsers(String file) throws IOException {
        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader =
                new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] res = line.split(" ");
            putUser(res[0], res[1]);
        }

        bufferedReader.close();
    }

    public static boolean isAuthorized(String name, String pwd){
        return (users.containsKey(name)) && (users.get(name).isPWdOk(pwd));
    }

    public static User getUser(String name){
        return users.get(name);
    }

    public static void putUser(String name, String pwd){
        users.put(name, new User(name, pwd));
    }
}

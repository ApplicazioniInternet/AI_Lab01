package it.polito.data;

import it.polito.utils.Utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/***
 * Questa classe è utilizzata per descrivere uno user. Contiene:
 *      - username = il nome con il quale si identifica lo user
 *      - password = la password decisa dall'utente
 *
 * ATTENZIONE: nel salvataggio della password c'è da tenere conto anche del SALE che è stato
 *      deciso di usare per salvarla in modo sicuro.
 */
public class User {
    private UserValue user;
    private byte[] digestPassword;

    public User(UserValue u) {
        this.user = u;
        try {
            digestPassword = Utilities.sha256(u.getUsername(), u.getPassword());
        } catch (NoSuchAlgorithmException e) {
            // La salvo come password normale se non esistesse l'algoritmo
            this.digestPassword = u.getPassword().getBytes();
        }
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public void setUsername(String username) {
        this.user.setUsername(username);
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    /*
        Funzione per controllare se la password inserita è corretta
        oppure no.
     */
    public boolean isPWdOk(String username, String pwd) {
        byte[] digestSent;
        try {
            digestSent = Utilities.sha256(username, pwd);
        } catch (NoSuchAlgorithmException e) {
            return Arrays.equals(this.digestPassword, pwd.getBytes(StandardCharsets.UTF_8));
        }
        return Arrays.equals(this.digestPassword, digestSent);
    }
}

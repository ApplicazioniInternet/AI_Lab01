package it.polito.data;

import it.polito.utils.Utilities;

import java.nio.charset.StandardCharsets;
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
    private String userName;
    private byte[] digestPassword;

    public User(UserValue u, boolean encoded) {
        userName = u.getUsername();
        try {
            if (!encoded)
                digestPassword = Utilities.sha256(u.getUsername(), u.getPassword());
            else
                digestPassword = u.getPassword().getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // La salvo come password normale se non esistesse l'algoritmo
            this.digestPassword = u.getPassword().getBytes(StandardCharsets.UTF_8);
        }
    }

    public String getUsername() {
        return this.userName;
    }

    public void setUsername(String username) {
        this.setUsername(username);
    }

    public byte[] getPassword() {
        return this.digestPassword;
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

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
    private String username;
    private String password;
    private byte[] digestPassword;

    public User(String username, String password, boolean encoded) {
        this.username = username;
        try {
            if (!encoded)
                digestPassword = Utilities.sha256(username, password);
            else
                digestPassword = password.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // La salvo come password normale se non esistesse l'algoritmo
            this.digestPassword = password.getBytes(StandardCharsets.UTF_8);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDigestPassword(byte[] digestPassword) {
        this.digestPassword = digestPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getDigestPassword() {
        return digestPassword;
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

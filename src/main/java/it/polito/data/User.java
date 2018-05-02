package it.polito.data;

import it.polito.utils.Utilities;
import org.postgresql.core.Encoding;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/***
 * Questa classe è utilizzata per descrivere uno user. Contiene:
 *      - username = il nome con il quale si identifica lo user
 *      - password = la password decisa dall'utente
 *
 * ATTENZIONE: nel salvataggio della password c'è da tenere conto anche del SALE che è stato
 *      deciso di usare per salvarla in modo sicuro.
 */
public class User {

    private UserInfo userInfo;
    public byte[] digestPassword;

    public static User newUser(UserInfo ui){
        return new User(ui.getUsername(), ui.getPassword(), false);
    }

    public User(String username, String password, boolean encoded) {
        this.userInfo = new UserInfo(username, password);
        try {
            if (!encoded)
                this.digestPassword = Utilities.sha256(username, password);
            else
                this.digestPassword = password.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // La salvo come password normale se non esistesse l'algoritmo
            this.digestPassword = password.getBytes(StandardCharsets.UTF_8);
        }
    }

    public void setUsername(String username) {
        userInfo.setUsername(username);
    }

    public void setPassword(String password) {
        userInfo.setPassword(password);
    }

    public void setDigestPassword(byte[] digestPassword) {
        this.digestPassword = digestPassword;
    }

    public String getUsername() {
        return userInfo.getUsername();
    }

    public String getPassword() {
        return userInfo.getPassword();
    }

    public byte[] getDigestPassword() {
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

    public boolean isPwdOk(String pwd){
        return pwd.equals(Arrays.toString(digestPassword));
    }

    public String toString() {
        return userInfo.getUsername() + "," + userInfo.getPassword() + "," + Arrays.toString(digestPassword);
    }
}

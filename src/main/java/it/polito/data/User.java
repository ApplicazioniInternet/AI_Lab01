package it.polito.data;

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

    public User(UserValue u) {
        this.user = u;
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

    public void setPassword(String password) {
        this.user.setPassword(password);
    }

    /*
        Funzione per controllare se la password inserita è corretta
        oppure no.
     */
    public boolean isPWdOk(String pwd) {
        return this.user.getPassword().equals(pwd);
    }
}

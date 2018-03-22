package it.polito.utils;

/***
 *  Custom exception che viene tirata quando ci viene passata una richiesta nulla.
 *  So che non dovrebbe succedere, ma non si sa mai...
 */
public class NullRequestException extends Exception {
    /*
        Costruttore senza parametri.
     */
    public NullRequestException() {}

    /*
        Costruttore nel caso si voglia inserire un custom message nella exception.
     */
    public NullRequestException(String message) {
        super(message);
    }
}

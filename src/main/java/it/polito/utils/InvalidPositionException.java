package it.polito.utils;

/***
 *  Custom exception creata per avvisare il servlet che, con i dati passatici dallo user,
 *  la velocità non va bene rispetto ai requisiti richiesti dalla traccia.
 */
public class InvalidPositionException extends Exception {
    /*
        Costruttore senza parametri.
     */
    public InvalidPositionException() {}

    /*
        Costruttore nel caso si voglia inserire un custom message nella exception.
     */
    public InvalidPositionException(String message) {
        super(message);
    }
}

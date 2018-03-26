package it.polito.utils;

public class UnauthorizedException extends Exception{
    /*
            Costruttore senza parametri.
         */
    public UnauthorizedException() {}

    /*
        Costruttore nel caso si voglia inserire un custom message nella exception.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}

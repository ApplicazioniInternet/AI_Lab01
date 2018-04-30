package it.polito.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/***
 *  Questa è una classe che ha lo scopo di contenere tutte le macro o eventuali costanti del progetto.
 */
public class Utilities {
    public static final double MAX_SPEED = 100;
    public static final String encAlgorithm = "SHA-256";
    public static final String urlDB  = "jdbc:postgresql:@127.0.0.1:5432:LabAI";
    public static final String DBuser = "bugged_group";
    public static final String DBpwd  = "bugged_group";

    public static byte[] sha256(String str1, String str2) throws NoSuchAlgorithmException {
        // Se non c'è la possibilità di applicare l'algoritmo, cavoli suoi
        MessageDigest digest = MessageDigest.getInstance(encAlgorithm);
        return digest.digest(xorInputData(str1.getBytes(StandardCharsets.UTF_8),
                str2.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] xorInputData(byte[] arr1, byte[] arr2) {
        int size = Integer.max(arr1.length, arr2.length); // Prendo la lunghezza massima
        byte[] arr3 = new byte[size];
        int i = 0;
        for(byte b : arr3) { // Scorro su arr3 così so che arrivo fino alla fino, "b" è una dummy variable
            // Se sono oltre la lunghezza faccio lo xor con un numero a caso, ho scelto 0 io, ma si poteva benissimo mettere 1
            byte first  = (i < arr1.length? arr1[i] : (byte) 0);
            byte second = (i < arr2.length? arr2[i] : (byte) 0);
            arr3[i] = (byte)(first ^ second);
        }

        return arr3;
    }
}

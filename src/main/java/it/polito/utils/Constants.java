package it.polito.utils;

public class Constants {
    public static final double validValueLowerBound = -180;
    public static final double validValueUpperBound = 180;
    public static final long minTimestamp = 1520238600; // Monday, 05-Mar-18 08:30:00 UTC
    public static final double MAX_SPEED = 100;
    public static final String encAlgorithm = "SHA-256";
    public static final String urlDB  = "jdbc:postgresql:LabAI";
    public static final String DBuser = "bugged_group";
    public static final String DBpwd  = "bugged_group";
    public static final String DBname = "LabAI";

    /* *
    *
    * docker volume create lab2
    *
    * docker run --name=postgis -d
    * -e POSTGRES_USER=bugged_group
    * -e POSTGRES_PASS=bugged_group
    * -e POSTGRES_DBNAME=LabAI
    * -e ALLOW_IP_RANGE=0.0.0.0/0
    * -p 5432:5432
    * -v lab2:/var/lib/postgresql
    * --restart=always
    * kartoza/postgis:9.6-2.4
    *
    * pinco - computer1
    * pallino - computer2
    *
    * */

}

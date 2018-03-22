package it.polito.data;

/***
 * Classe che serve a salvare tutte le informazioni per una singola posizione. Contiene:
 *      - latitudine
 *      - longitudine
 *      - timestamp
 */
public class Position {
    private double lat, lon;
    private long timestamp;

    public Position(double lat, double lon, long time) {
        this.lat = lat;
        this.lon = lon;
        this.timestamp = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTime() {
        return timestamp;
    }

    public void setTime(long time) {
        this.timestamp = time;
    }

    /*
        Questa funzione ci è utile per calcolare la distanza tra me e una posizione
        passataci come parametro.
     */
    public double getDistanceFrom(Position p){
        return Haversine.distance(this.lat, this.lon, p.getLat(), p.getLon());
    }
}

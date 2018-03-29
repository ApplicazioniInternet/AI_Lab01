package it.polito.data;

/***
 * Classe che serve a salvare tutte le informazioni per una singola posizione. Contiene:
 *      - latitudine
 *      - longitudine
 *      - timestamp
 */
public class Position {
    private PositionValue position;

    public Position(double lat, double lon, long time){
        this.position = new PositionValue();
        this.position.setLatitude(lat);
        this.position.setLongitude(lon);
        this.position.setTimestamp(time);
    }

    public Position(PositionValue pos) {
        this.position = pos;
    }

    public double getLatitude() {
        return this.position.getLatitude();
    }

    public void setLatitude(double lat) {
        this.position.setLatitude(lat);
    }

    public double getLongitude() {
        return this.position.getLongitude();
    }

    public void setLongitude(double lon) {
        this.position.setLongitude(lon);
    }

    public long getTimestamp() {
        return this.position.getTimestamp();
    }

    public void setTimestamp(long time) {
        this.position.setTimestamp(time);
    }

    /*
        Questa funzione ci è utile per calcolare la distanza tra me e una posizione
        passataci come parametro.
     */
    public double getDistanceFrom(Position p) {
        return Haversine.distance(this.position.getLatitude(),
                this.position.getLongitude(),
                p.getLatitude(),
                p.getLongitude()
        );
    }
}

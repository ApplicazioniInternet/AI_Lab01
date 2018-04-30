package it.polito.data;

public class Position {
    private double latitude;
    private double longitude;
    private long timestamp;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    /*
    Questa funzione ci è utile per calcolare la distanza tra me e una posizione
    passataci come parametro.
     */
    public double getDistanceFrom(Position p) {
        return Haversine.distance(this.latitude,
                this.longitude,
                p.getLatitude(),
                p.getLongitude()
        );
    }
}

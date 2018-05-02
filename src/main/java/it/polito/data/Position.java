package it.polito.data;

public class Position {
    private PositionValue pv;

    public Position(){
        pv = new PositionValue();
    }

    public Position(PositionValue value){
        this.pv = value;
    }

    public double getLatitude() {
        return pv.getLatitude();
    }

    public void setLatitude(double latitude) {
        pv.setLatitude(latitude);
    }

    public double getLongitude() {
        return pv.getLongitude();
    }

    public void setLongitude(double longitude) {
        pv.setLongitude(longitude);
    }

    public long getTimestamp() { return pv.getTimestamp(); }

    public void setTimestamp(long timestamp) { pv.setTimestamp(timestamp); }

    /*
    Questa funzione ci è utile per calcolare la distanza tra me e una posizione
    passataci come parametro.
     */
    public double getDistanceFrom(Position p) {
        return Haversine.distance(pv.getLatitude(),
                pv.getLongitude(),
                p.getLatitude(),
                p.getLongitude()
        );
    }
}

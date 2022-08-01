package common.model;

public class Location {
    private double longitude;
    private double latitude;
    public Location(double latitude, double longitude) {

        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public void setLongitude(double longitude) {

        this.longitude = longitude;
    }

    public Location() {

    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public double getLatitude() {

        return latitude;
    }
}

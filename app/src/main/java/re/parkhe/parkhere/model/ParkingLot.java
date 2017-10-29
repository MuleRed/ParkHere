package re.parkhe.parkhere.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ryan on 10/28/17.
 */

public class ParkingLot {
    private double latitude;
    private double longitude;

    public ParkingLot(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}

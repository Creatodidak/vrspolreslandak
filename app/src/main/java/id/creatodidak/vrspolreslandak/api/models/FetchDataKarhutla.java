package id.creatodidak.vrspolreslandak.api.models;

public class FetchDataKarhutla {
    private double latitude;
    private double longitude;
    private int confidence;
    private String locationName;

    public FetchDataKarhutla(double latitude, double longitude, int confidence, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.confidence = confidence;
        this.locationName = locationName;
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

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

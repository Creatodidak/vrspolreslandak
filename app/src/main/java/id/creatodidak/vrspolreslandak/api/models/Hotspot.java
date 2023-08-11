package id.creatodidak.vrspolreslandak.api.models;

public class Hotspot {
    private int id;
    private String dataId;
    private double latitude;
    private double longitude;
    private int confidence;
    private int radius;
    private String location;
    private String wilayah;
    private String response;
    private String status;
    private String notif;
    private String createdAt;
    private String updatedAt;

    public Hotspot() {
        // Konstruktor kosong diperlukan untuk operasi database
    }

    public Hotspot(int id, String dataId, double latitude, double longitude,
                   int confidence, int radius, String location, String wilayah,
                   String response, String status, String notif,
                   String createdAt, String updatedAt) {
        this.id = id;
        this.dataId = dataId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.confidence = confidence;
        this.radius = radius;
        this.location = location;
        this.wilayah = wilayah;
        this.response = response;
        this.status = status;
        this.notif = notif;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

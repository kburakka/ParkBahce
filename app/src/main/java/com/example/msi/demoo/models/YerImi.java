package com.example.msi.demoo.models;

public class YerImi {
    private String yerImiName;
    private double zoom;
    private double Latitude;
    private double Longitude;

    public YerImi(String yerImiName, double zoom, double latitude, double longitude) {
        this.yerImiName = yerImiName;
        this.zoom = zoom;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getYerImiName() {
        return yerImiName;
    }
    public void setYerImiName(String yerImiName) {
        this.yerImiName = yerImiName;
    }

    public double getZoom() {
        return zoom;
    }
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getLatitude() {
        return Latitude;
    }
    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

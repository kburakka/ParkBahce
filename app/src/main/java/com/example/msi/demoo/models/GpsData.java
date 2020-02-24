package com.example.msi.demoo.models;

public class GpsData {
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private long messageTime;

    public GpsData(Double latitude, Double longitude, Double altitude, long messageTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.messageTime = messageTime;
    }


    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public long getMessageTime() {
        return messageTime;
    }
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        return "GpsData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", messageTime=" + messageTime +
                '}';
    }
}

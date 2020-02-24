package com.example.msi.demoo.models;

public class IlIlceMahalle {
    private String id;
    private String text;
    private String geoJson;

    public IlIlceMahalle(String id, String text, String geoJson) {
        this.id = id;
        this.text = text;
        this.geoJson = geoJson;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getGeoJson() {
        return geoJson;
    }
    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }
}

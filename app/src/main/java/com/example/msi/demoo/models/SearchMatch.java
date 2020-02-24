package com.example.msi.demoo.models;

public class SearchMatch {
    private String type;
    private String ilceId;
    private String ilceAd;
    private String mahId;
    private String mahAd;

    public SearchMatch(String type, String ilceId, String ilceAd) {
        this.type = type;
        this.ilceId = ilceId;
        this.ilceAd = ilceAd;
    }

    public SearchMatch(String type, String mahId, String mahAd, String ilceId) {
        this.type = type;
        this.mahId = mahId;
        this.mahAd = mahAd;
        this.ilceId = ilceId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getIlceId() {
        return ilceId;
    }
    public void setIlceId(String ilceId) {
        this.ilceId = ilceId;
    }

    public String getIlceAd() {
        return ilceAd;
    }
    public void setIlceAd(String ilceAd) {
        this.ilceAd = ilceAd;
    }

    public String getMahId() {
        return mahId;
    }
    public void setMahId(String mahId) {
        this.mahId = mahId;
    }

    public String getMahAd() {
        return mahAd;
    }
    public void setMahAd(String mahAd) {
        this.mahAd = mahAd;
    }
}

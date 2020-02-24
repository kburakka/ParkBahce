package com.example.msi.demoo.models;

import android.net.Uri;

public class KatmanMedia{
    private Uri uri;
    private String type;
    private Boolean isExistService;
    private FileDataInfo fileDataInfo;

    public KatmanMedia(Uri uri, String type, Boolean isExistService, FileDataInfo fileDataInfo) {
        this.uri = uri;
        this.type = type;
        this.isExistService = isExistService;
        this.fileDataInfo = fileDataInfo;
    }

    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Boolean getExistService() {
        return isExistService;
    }
    public void setExistService(Boolean existService) {
        isExistService = existService;
    }

    public FileDataInfo getFileDataInfo() {
        return fileDataInfo;
    }
    public void setFileDataInfo(FileDataInfo fileDataInfo) {
        this.fileDataInfo = fileDataInfo;
    }


    @Override
    public String toString() {
        return "KatmanMedia{" +
                "uri=" + uri +
                ", type='" + type + '\'' +
                ", isExistService=" + isExistService +
                ", fileDataInfo=" + fileDataInfo +
                '}';
    }
}

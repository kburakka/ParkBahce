package com.example.msi.demoo.models;

public class FileDataInfo{
    private Integer id;
    private String fname;
    private String size;
    private Integer touchBy;
    private String touchDate;
    private String username;

    public FileDataInfo(Integer id, String fname, String size, Integer touchBy, String touchDate, String username) {
        this.id = id;
        this.fname = fname;
        this.size = size;
        this.touchBy = touchBy;
        this.touchDate = touchDate;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public Integer getTouchBy() {
        return touchBy;
    }
    public void setTouchBy(Integer touchBy) {
        this.touchBy = touchBy;
    }

    public String getTouchDate() {
        return touchDate;
    }
    public void setTouchDate(String touchDate) {
        this.touchDate = touchDate;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "FileDataInfo{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", size='" + size + '\'' +
                ", touchBy=" + touchBy +
                ", touchDate='" + touchDate + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

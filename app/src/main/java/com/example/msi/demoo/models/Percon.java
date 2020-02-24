package com.example.msi.demoo.models;

import com.example.msi.demoo.models.LayerModel;

import java.util.List;

public class Percon {
    private boolean success;
    private String username;
    private int userId;
    private List<LayerModel> layers;

    public Percon(boolean success, String username, int userId, List<LayerModel> layers) {
        this.success = success;
        this.username = username;
        this.userId = userId;
        this.layers = layers;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<LayerModel> getLayers() {
        return layers;
    }
    public void setLayers(List<LayerModel> layers) {
        this.layers = layers;
    }

}

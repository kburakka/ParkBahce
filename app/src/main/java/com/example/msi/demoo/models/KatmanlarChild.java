package com.example.msi.demoo.models;

import android.graphics.Bitmap;

public class KatmanlarChild {
    private Bitmap bitmap;
    private LayerStyleData layerStyleData;

    public KatmanlarChild(Bitmap bitmap, LayerStyleData layerStyleData) {
        this.bitmap = bitmap;
        this.layerStyleData = layerStyleData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LayerStyleData getLayerStyleData() {
        return layerStyleData;
    }
    public void setLayerStyleData(LayerStyleData layerStyleData) {
        this.layerStyleData = layerStyleData;
    }
}

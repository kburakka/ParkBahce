package com.example.msi.demoo.models;

import android.widget.RelativeLayout;

public class PropertyObject {
    private LayerField layerField;
    private String value;
    private Object object;
    private RelativeLayout relativeLayoutParent;

    public PropertyObject(LayerField layerField, String value, Object object, RelativeLayout relativeLayoutParent) {
        this.layerField = layerField;
        this.value = value;
        this.object = object;
        this.relativeLayoutParent = relativeLayoutParent;
    }

    public LayerField getLayerField() {
        return layerField;
    }
    public void setLayerField(LayerField layerField) {
        this.layerField = layerField;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }

    public RelativeLayout getRelativeLayoutParent() {
        return relativeLayoutParent;
    }
    public void setRelativeLayoutParent(RelativeLayout relativeLayoutParent) {
        this.relativeLayoutParent = relativeLayoutParent;
    }
}

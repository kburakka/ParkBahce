package com.example.msi.demoo.models;

import java.util.List;

public class LayerModel {
    private int id;
    private String layer;
    private String alias;
    private String type;
    private String workspace;

    /*Permission Values:
                        1 = read
                        2 = 1 + insert
                        3 = 2 + update
                        4 = 3 + delete
     */
    private Integer permision;
    private Boolean required;
    private List<LayerField> fields;
    private LayerStyle style;

    public LayerModel(int id, String layer, String alias, String type, String workspace, Integer permision, Boolean required, List<LayerField> fields, LayerStyle style) {
        this.id = id;
        this.layer = layer;
        this.alias = alias;
        this.type = type;
        this.workspace = workspace;
        this.permision = permision;
        this.required = required;
        this.fields = fields;
        this.style = style;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getLayer() {
        return layer;
    }
    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getWorkspace() {
        return workspace;
    }
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public Integer getPermision() {
        return permision;
    }
    public void setPermision(Integer permision) {
        this.permision = permision;
    }

    public Boolean getRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<LayerField> getFields() {
        return fields;
    }
    public void setFields(List<LayerField> fields) {
        this.fields = fields;
    }

    public LayerStyle getStyle() {
        return style;
    }
    public void setStyle(LayerStyle style) {
        this.style = style;
    }
}

package com.example.msi.demoo.models;

import java.util.List;

public class LayerField {
    private int id;
    private String layer;
    private String field;
    private String alias;
    private Boolean domain;
    private String domainTable;
    private Integer length;
    private String type;
    private Integer position;
    private Boolean required;
    private Integer permision;
    private List<LayerFieldCodedValue> codedValues;


    public LayerField(int id, String layer, String field, String alias, Boolean domain, String domainTable, Integer length, String type, Integer position, Boolean required, Integer permision, List<LayerFieldCodedValue> codedValues) {
        this.id = id;
        this.layer = layer;
        this.field = field;
        this.alias = alias;
        this.domain = domain;
        this.domainTable = domainTable;
        this.length = length;
        this.type = type;
        this.position = position;
        this.required = required;
        this.permision = permision;
        this.codedValues = codedValues;
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

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean isDomain() {
        return domain;
    }
    public void setDomain(Boolean domain) {
        this.domain = domain;
    }

    public String getDomainTable() {
        return domainTable;
    }
    public void setDomainTable(String domainTable) {
        this.domainTable = domainTable;
    }

    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean isRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPermision() {
        return permision;
    }
    public void setPermision(Integer permision) {
        this.permision = permision;
    }

    public List<LayerFieldCodedValue> getCodedValues() {
        return codedValues;
    }
    public void setCodedValues(List<LayerFieldCodedValue> codedValues) {
        this.codedValues = codedValues;
    }
}

package com.example.msi.demoo.models;

public class LayerStyleData {
    private int id;
    private Integer codedId;

    private Integer lineWidth;
    private String lineColor;
    private Double lineOpacity;
    private String fillColor;
    private Double fillOpacity;

    private String image;
    private Integer width;
    private Integer height;
    private Integer offsetX;
    private Integer offsetY;
    private String layerName;
    private String fieldName;
    private String codedName;

    private String styleName;
    private Integer minZoom;
    private Integer maxZoom;
    private Boolean isLabel;
    private Integer layerType;
    private String wellKnownName;
    private String pointColor;
    private Integer pointSize;
    private Integer pointRotation;



//    public LayerStyleData(int id, Integer codedId, Integer lineWidth, String lineColor, Double lineOpacity, String fillColor, Double fillOpacity,
//                          String image, Integer width, Integer height, Integer offsetX, Integer offsetY, String layerName, String fieldName, String codedName) {
//        this.id = id;
//        this.codedId = codedId;
//        this.lineWidth = lineWidth;
//        this.lineColor = lineColor;
//        this.lineOpacity = lineOpacity;
//        this.fillColor = fillColor;
//        this.fillOpacity = fillOpacity;
//        this.image = image;
//        this.width = width;
//        this.height = height;
//        this.offsetX = offsetX;
//        this.offsetY = offsetY;
//        this.layerName = layerName;
//        this.fieldName = fieldName;
//        this.codedName = codedName;
//    }


    public LayerStyleData(int id, Integer codedId, Integer lineWidth, String lineColor, Double lineOpacity, String fillColor, Double fillOpacity, String image, Integer width, Integer height, Integer offsetX, Integer offsetY, String layerName, String fieldName, String codedName, Integer minZoom, Integer maxZoom, Boolean isLabel, Integer layerType, String styleName) {
        this.id = id;
        this.codedId = codedId;
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
        this.lineOpacity = lineOpacity;
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.image = image;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.layerName = layerName;
        this.fieldName = fieldName;
        this.codedName = codedName;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.isLabel = isLabel;
        this.layerType = layerType;
        this.styleName = styleName;
    }


    public LayerStyleData(int id, Integer codedId, Integer lineWidth, String lineColor, Double lineOpacity, String fillColor, Double fillOpacity, String image, Integer width, Integer height, Integer offsetX, Integer offsetY, String layerName, String fieldName, String codedName, String styleName, Integer minZoom, Integer maxZoom, Boolean isLabel, Integer layerType, String wellKnownName, String pointColor, Integer pointSize, Integer pointRotation) {
        this.id = id;
        this.codedId = codedId;
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
        this.lineOpacity = lineOpacity;
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.image = image;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.layerName = layerName;
        this.fieldName = fieldName;
        this.codedName = codedName;
        this.styleName = styleName;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.isLabel = isLabel;
        this.layerType = layerType;
        this.wellKnownName = wellKnownName;
        this.pointColor = pointColor;
        this.pointSize = pointSize;
        this.pointRotation = pointRotation;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getCodedId() {
        return codedId;
    }
    public void setCodedId(Integer codedId) {
        this.codedId = codedId;
    }

    public Integer getLineWidth() {
        return lineWidth;
    }
    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getLineColor() {
        return lineColor;
    }
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public Double getLineOpacity() {
        return lineOpacity;
    }
    public void setLineOpacity(Double lineOpacity) {
        this.lineOpacity = lineOpacity;
    }

    public String getFillColor() {
        return fillColor;
    }
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public Double getFillOpacity() {
        return fillOpacity;
    }
    public void setFillOpacity(Double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getOffsetX() {
        return offsetX;
    }
    public void setOffsetX(Integer offsetX) {
        this.offsetX = offsetX;
    }

    public Integer getOffsetY() {
        return offsetY;
    }
    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    public String getLayerName() {
        return layerName;
    }
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCodedName() {
        return codedName;
    }
    public void setCodedName(String codedName) {
        this.codedName = codedName;
    }


    public Integer getMinZoom() {
        return minZoom;
    }
    public void setMinZoom(Integer minZoom) {
        this.minZoom = minZoom;
    }

    public Integer getMaxZoom() {
        return maxZoom;
    }
    public void setMaxZoom(Integer maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Boolean getLabel() {
        return isLabel;
    }
    public void setLabel(Boolean label) {
        isLabel = label;
    }

    public Integer getLayerType() {
        return layerType;
    }
    public void setLayerType(Integer layerType) {
        this.layerType = layerType;
    }

    public String getStyleName() {
        return styleName;
    }
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getWellKnownName() {
        return wellKnownName;
    }
    public void setWellKnownName(String wellKnownName) {
        this.wellKnownName = wellKnownName;
    }

    public String getPointColor() {
        return pointColor;
    }
    public void setPointColor(String pointColor) {
        this.pointColor = pointColor;
    }

    public Integer getPointSize() {
        return pointSize;
    }
    public void setPointSize(Integer pointSize) {
        this.pointSize = pointSize;
    }

    public Integer getPointRotation() {
        return pointRotation;
    }
    public void setPointRotation(Integer pointRotation) {
        this.pointRotation = pointRotation;
    }
}

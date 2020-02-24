package com.example.msi.demoo.models;

import java.util.List;

public class LayerStyle {
    private String type;
    private List<LayerStyleData> datas;

    public LayerStyle(String type, List<LayerStyleData> datas) {
        this.type = type;
        this.datas = datas;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<LayerStyleData> getDatas() {
        return datas;
    }
    public void setDatas(List<LayerStyleData> datas) {
        this.datas = datas;
    }
}

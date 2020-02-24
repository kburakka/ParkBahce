package com.example.msi.demoo.models;

import android.graphics.Bitmap;

import java.util.List;

public class KatmanlarParent {

    private boolean openOrClose;
    private boolean checkedOrUnchecked;
    private Bitmap bitmap;
    private LayerModel layer;
    private List<KatmanlarChild> katmanlarChildList;

    public KatmanlarParent(boolean openOrClose, boolean checkedOrUnchecked, Bitmap bitmap, LayerModel layer, List<KatmanlarChild> katmanlarChildList) {
        this.openOrClose = openOrClose;
        this.checkedOrUnchecked = checkedOrUnchecked;
        this.bitmap = bitmap;
        this.layer = layer;
        this.katmanlarChildList = katmanlarChildList;
    }

    public boolean isOpenOrClose() {
        return openOrClose;
    }
    public void setOpenOrClose(boolean openOrClose) {
        this.openOrClose = openOrClose;
    }

    public boolean isCheckedOrUnchecked() {
        return checkedOrUnchecked;
    }
    public void setCheckedOrUnchecked(boolean checkedOrUnchecked) {
        this.checkedOrUnchecked = checkedOrUnchecked;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<KatmanlarChild> getKatmanlarChildList() {
        return katmanlarChildList;
    }
    public void setKatmanlarChildList(List<KatmanlarChild> katmanlarChildList) {
        this.katmanlarChildList = katmanlarChildList;
    }

    public LayerModel getLayer() {
        return layer;
    }
    public void setLayer(LayerModel layer) {
        this.layer = layer;
    }
}

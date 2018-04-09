package com.app.bespokino.model;

/**
 * Created by bespokino on 10/9/2017 AD.
 */

public class FabricContrast {


    int fabricId;
    String image;
    boolean isSelected=false;

    public FabricContrast(int fabricId, String image, boolean isSelected) {
        this.fabricId = fabricId;
        this.image = image;
        this.isSelected = isSelected;
    }

    public FabricContrast(int fabricId, boolean isSelected) {
        this.fabricId = fabricId;
        this.isSelected = isSelected;
    }


    public FabricContrast(int fabricId, String image) {
        this.fabricId = fabricId;
        this.image = image;
    }

    public int getFabricId() {
        return fabricId;
    }

    public void setFabricId(int fabricId) {
        this.fabricId = fabricId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

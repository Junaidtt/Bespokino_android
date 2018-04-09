package com.app.bespokino.model;

/**
 * Created by bespokino on 7/31/2017 AD.
 */

public class FabricModel {

    private String fabric_img;
    private String shirt_img;
    private int fabricID;
    private  int addup;


    public FabricModel(String fabric_img, String shirt_img, int fabricID, int addup) {
        this.fabric_img = fabric_img;
        this.shirt_img = shirt_img;
        this.fabricID = fabricID;
        this.addup = addup;
    }

    public FabricModel(String fabric_img, String shirt_img, int fabricID) {
        this.fabric_img = fabric_img;
        this.shirt_img = shirt_img;
        this.fabricID = fabricID;
    }

    public FabricModel(String fabric_img, String shirt_img) {
        this.fabric_img = fabric_img;
        this.shirt_img = shirt_img;
    }

    public int getAddup() {
        return addup;
    }

    public void setAddup(int addup) {
        this.addup = addup;
    }

    public int getFabricID() {
        return fabricID;
    }

    public void setFabricID(int fabricID) {
        this.fabricID = fabricID;
    }

    public String getShirt_img() {
        return shirt_img;
    }

    public void setShirt_img(String shirt_img) {
        this.shirt_img = shirt_img;
    }

    public FabricModel(String fabric_img) {
        this.fabric_img = fabric_img;
    }

    public FabricModel() {

    }

    public String getFabric_img() {
        return fabric_img;
    }

    public void setFabric_img(String fabric_img) {
        this.fabric_img = fabric_img;
    }
}

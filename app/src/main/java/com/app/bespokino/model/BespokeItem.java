package com.app.bespokino.model;

/**
 * Created by bespokino on 26/3/2018 AD.
 */

public class BespokeItem {

     String itemName;
     int imageName;

    public BespokeItem(String itemName, int imageName) {
        this.itemName = itemName;
        this.imageName = imageName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImageName() {
        return imageName;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }
}

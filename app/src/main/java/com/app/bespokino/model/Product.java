package com.app.bespokino.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 8/7/2017 AD.
 */

public class Product {

    List<ItemModel> items = new ArrayList<>();
    String fabricImage;
    String shirtNumber;
    float itemPrice;


    public Product(List<ItemModel> items, String fabricImage, String shirtNumber, float itemPrice) {
        this.items = items;
        this.fabricImage = fabricImage;
        this.shirtNumber = shirtNumber;
        this.itemPrice = itemPrice;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public String getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(String shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public String getFabricImage() {
        return fabricImage;
    }

    public void setFabricImage(String fabricImage) {
        this.fabricImage = fabricImage;
    }



}

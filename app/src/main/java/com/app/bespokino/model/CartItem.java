package com.app.bespokino.model;

import java.util.List;

/**
 * Created by bespokino on 8/2/2017 AD.
 */

public class CartItem {

    String name;
    String price;
    String image;

    public CartItem(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public CartItem(List<String> test) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

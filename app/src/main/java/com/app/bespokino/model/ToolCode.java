package com.app.bespokino.model;

/**
 * Created by bespokino on 10/11/2017 AD.
 */

public class ToolCode {

    int imageColor;
    double imageValue;

    public ToolCode(int imageColor, double imageValue) {
        this.imageColor = imageColor;
        this.imageValue = imageValue;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }

    public double getImageValue() {
        return imageValue;
    }

    public void setImageValue(double imageValue) {
        this.imageValue = imageValue;
    }
}

package com.app.bespokino.model;

/**
 * Created by bespokino on 9/15/2017 AD.
 */

public class ThreadModel {


    int imgID;
    int imgCode;
    String imageLink;
    boolean isSelected = false;
    String contrast;


    public ThreadModel(int imgCode, String imageLink,String contrast) {
        this.imgCode = imgCode;
        this.imageLink = imageLink;
        this.contrast = contrast;
    }

    public ThreadModel(int imgCode, String imageLink) {
        this.imgCode = imgCode;
        this.imageLink = imageLink;
    }

    public ThreadModel(boolean isSelected,int imgID, int imgCode) {
        this.isSelected = isSelected;
        this.imgID = imgID;
        this.imgCode = imgCode;
    }

    public String getContrast() {
        return contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getImgCode() {
        return imgCode;
    }

    public void setImgCode(int imgCode) {
        this.imgCode = imgCode;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package com.app.bespokino.model;

/**
 * Created by bespokino on 8/16/2017 AD.
 */

public class FrontToolValues {

    String neck;
    String biceps;
    String cuff;
    String length;
    String sleeveLength;

    public FrontToolValues(String neck, String biceps, String cuff, String length, String sleeveLength) {
        this.neck = neck;
        this.biceps = biceps;
        this.cuff = cuff;
        this.length = length;
        this.sleeveLength = sleeveLength;
    }


    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getBiceps() {
        return biceps;
    }

    public void setBiceps(String biceps) {
        this.biceps = biceps;
    }

    public String getCuff() {
        return cuff;
    }

    public void setCuff(String cuff) {
        this.cuff = cuff;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(String sleeveLength) {
        this.sleeveLength = sleeveLength;
    }
}

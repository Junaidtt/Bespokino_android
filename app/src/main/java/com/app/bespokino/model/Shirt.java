package com.app.bespokino.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by bespokino on 8/2/2017 AD.
 */

public class Shirt implements Parcelable {

    String fabricName;
    String collar;
    String cuff;
    String monogram;
    ArrayList<String> addOptions= new ArrayList<>();

    public Shirt(String fabricName, String collar, String cuff, String monogram) {
        this.fabricName = fabricName;
        this.collar = collar;
        this.cuff = cuff;
        this.monogram = monogram;
    }

    public Shirt(String fabricName, String collar, String cuff, String monogram, ArrayList<String> addOptions) {
        this.fabricName = fabricName;
        this.collar = collar;
        this.cuff = cuff;
        this.monogram = monogram;
        this.addOptions = addOptions;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getCollar() {
        return collar;
    }

    public void setCollar(String collar) {
        this.collar = collar;
    }

    public String getCuff() {
        return cuff;
    }

    public void setCuff(String cuff) {
        this.cuff = cuff;
    }

    public String getMonogram() {
        return monogram;
    }

    public void setMonogram(String monogram) {
        this.monogram = monogram;
    }

    //Parcelable


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fabricName);
        dest.writeString(this.collar);
        dest.writeString(this.cuff);
        dest.writeString(this.monogram);
    }

    protected Shirt(Parcel in) {
        this.fabricName = in.readString();
        this.collar = in.readString();
        this.cuff = in.readString();
        this.monogram = in.readString();
    }

    public static final Parcelable.Creator<Shirt> CREATOR = new Parcelable.Creator<Shirt>() {
        @Override
        public Shirt createFromParcel(Parcel source) {
            return new Shirt(source);
        }

        @Override
        public Shirt[] newArray(int size) {
            return new Shirt[size];
        }
    };
}

package com.app.bespokino.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bespokino on 8/1/2017 AD.
 */

public class ItemModel implements Parcelable {


    boolean isSelected;
    String  itemName;
    Integer itemImage;
    String  itemCode;
    int optionValude;
    int choiceID;

    public ItemModel(boolean isSelected, String itemName, Integer itemImage, String itemCode, int optionValude, int choiceID) {
        this.isSelected = isSelected;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemCode = itemCode;
        this.optionValude = optionValude;
        this.choiceID = choiceID;
    }

    public ItemModel(boolean isSelected, String itemName, Integer itemImage, String itemCode) {
        this.isSelected = isSelected;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemCode = itemCode;
    }

    public int getOptionValude() {
        return optionValude;
    }

    public void setOptionValude(int optionValude) {
        this.optionValude = optionValude;
    }

    public int getChoiceID() {
        return choiceID;
    }

    public void setChoiceID(int choiceID) {
        this.choiceID = choiceID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemImage() {
        return itemImage;
    }

    public void setItemImage(Integer itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.itemName);
        dest.writeValue(this.itemImage);
        dest.writeString(this.itemCode);
    }

    protected ItemModel(Parcel in) {
        this.isSelected = in.readByte() != 0;
        this.itemName = in.readString();
        this.itemImage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemCode = in.readString();
    }

    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel source) {
            return new ItemModel(source);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };



}

package com.app.bespokino.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 8/11/2017 AD.
 */

public class CartProduct {

    //[{"trackingID":"4380179 - 2221 - 1","image":"DSC_0026.jpg","customerID":2221.0,"Error":false,"orderNo":4380179.0,"paperNo":50541.0},{"trackingID":"4380179 - 2221 - 2","image":"s6.jpg","customerID":2221.0,"Error":false,"orderNo":4380179.0,"paperNo":50541.0},{"trackingID":"4380179 - 2221 - 3","image":"DSC_0023.jpg","customerID":2221.0,"Error":false,"orderNo":4380179.0,"paperNo":50541.0}]

    String trackingID;
    int customerID;
    int orderNo;
    int paperNo;
    String fabricImg;
    String shirtCount;
    double shirtPrice;

    public CartProduct(String trackingID, int customerID, int orderNo, int paperNo, String fabricImg, String shirtCount, double shirtPrice) {
        this.trackingID = trackingID;
        this.customerID = customerID;
        this.orderNo = orderNo;
        this.paperNo = paperNo;
        this.fabricImg = fabricImg;
        this.shirtCount = shirtCount;
        this.shirtPrice = shirtPrice;
    }

    public double getShirtPrice() {
        return shirtPrice;
    }

    public void setShirtPrice(double shirtPrice) {
        this.shirtPrice = shirtPrice;
    }

    public String getShirtCount() {
        return shirtCount;
    }

    public void setShirtCount(String shirtCount) {
        this.shirtCount = shirtCount;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getPaperNo() {
        return paperNo;
    }

    public void setPaperNo(int paperNo) {
        this.paperNo = paperNo;
    }

    public String getFabricImg() {
        return fabricImg;
    }

    public void setFabricImg(String fabricImg) {
        this.fabricImg = fabricImg;
    }
}

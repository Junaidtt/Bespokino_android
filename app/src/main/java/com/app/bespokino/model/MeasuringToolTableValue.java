package com.app.bespokino.model;

/**
 * Created by bespokino on 10/11/2017 AD.
 */

public class MeasuringToolTableValue {

    String modelNo;
    double shoulderMaster;
    double sleeveMaster;
    double chestMaster;
    double waistMaster;
    double hipsMaster;
    double lengthMaster;
    double bicepsMaster;
    double cuffMaster;
    double nectMaster;

    public MeasuringToolTableValue(String modelNo, double shoulderMaster, double sleeveMaster, double chestMaster, double waistMaster, double hipsMaster, double lengthMaster, double bicepsMaster, double cuffMaster, double nectMaster) {
        this.modelNo = modelNo;
        this.shoulderMaster = shoulderMaster;
        this.sleeveMaster = sleeveMaster;
        this.chestMaster = chestMaster;
        this.waistMaster = waistMaster;
        this.hipsMaster = hipsMaster;
        this.lengthMaster = lengthMaster;
        this.bicepsMaster = bicepsMaster;
        this.cuffMaster = cuffMaster;
        this.nectMaster = nectMaster;
    }


    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public double getShoulderMaster() {
        return shoulderMaster;
    }

    public void setShoulderMaster(double shoulderMaster) {
        this.shoulderMaster = shoulderMaster;
    }

    public double getSleeveMaster() {
        return sleeveMaster;
    }

    public void setSleeveMaster(double sleeveMaster) {
        this.sleeveMaster = sleeveMaster;
    }

    public double getChestMaster() {
        return chestMaster;
    }

    public void setChestMaster(double chestMaster) {
        this.chestMaster = chestMaster;
    }

    public double getWaistMaster() {
        return waistMaster;
    }

    public void setWaistMaster(double waistMaster) {
        this.waistMaster = waistMaster;
    }

    public double getHipsMaster() {
        return hipsMaster;
    }

    public void setHipsMaster(double hipsMaster) {
        this.hipsMaster = hipsMaster;
    }

    public double getLengthMaster() {
        return lengthMaster;
    }

    public void setLengthMaster(double lengthMaster) {
        this.lengthMaster = lengthMaster;
    }

    public double getBicepsMaster() {
        return bicepsMaster;
    }

    public void setBicepsMaster(double bicepsMaster) {
        this.bicepsMaster = bicepsMaster;
    }

    public double getCuffMaster() {
        return cuffMaster;
    }

    public void setCuffMaster(double cuffMaster) {
        this.cuffMaster = cuffMaster;
    }

    public double getNectMaster() {
        return nectMaster;
    }

    public void setNectMaster(double nectMaster) {
        this.nectMaster = nectMaster;
    }
}

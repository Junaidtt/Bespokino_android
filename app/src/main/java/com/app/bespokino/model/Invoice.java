package com.app.bespokino.model;

/**
 * Created by bespokino on 9/29/2017 AD.
 */

public class Invoice {

   double customeMadeShirt;
    double fabricUpgrade;
    double stylingAddup;

    public Invoice(double customeMadeShirt, double fabricUpgrade, double stylingAddup) {
        this.customeMadeShirt = customeMadeShirt;
        this.fabricUpgrade = fabricUpgrade;
        this.stylingAddup = stylingAddup;
    }

    public double getCustomeMadeShirt() {
        return customeMadeShirt;
    }

    public void setCustomeMadeShirt(double customeMadeShirt) {
        this.customeMadeShirt = customeMadeShirt;
    }

    public double getFabricUpgrade() {
        return fabricUpgrade;
    }

    public void setFabricUpgrade(double fabricUpgrade) {
        this.fabricUpgrade = fabricUpgrade;
    }

    public double getStylingAddup() {
        return stylingAddup;
    }

    public void setStylingAddup(double stylingAddup) {
        this.stylingAddup = stylingAddup;
    }
}

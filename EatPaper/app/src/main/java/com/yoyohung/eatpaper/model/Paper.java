package com.yoyohung.eatpaper.model;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Arrays;

@IgnoreExtraProperties
public class Paper {
    public static final String FIELD_PAPER_ID = "paperID";
    public static final String FIELD_PAPER_TYPE = "paperType";
    public static final String FIELD_PAPER_NAME = "paperName";
    public static final String FIELD_PAPER_SIZE = "paperSize";
    public static final String FIELD_PAPER_WEIGHT = "paperWeight";
    public static final String FIELD_PAPER_COLOR = "paperColor";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_SUPPLY_COMPANY = "supplyCompany";
    public static final String FIELD_CURRENT_QUANTITY = "currentQuantity";
    public static final String FIELD_HISTORY = "history";

    private String paperID;
    private String paperType;
    private String paperName;
    private String paperSize;
    private int paperWeight;
    private String paperColor;
    private String unit;
    private String supplyCompany;
    private int currentQuantity;
    private Arrays history;

    public Paper() {}

    public Paper(String paperID,
            String paperType,
            String paperName,
            String paperSize,
            String paperColor,
            String unit,
            String supplyCompany,
            int paperWeight,
            int currentQuantity,
            Arrays history) {

        this.paperID = paperID;
        this.paperType = paperType;
        this.paperName = paperName;
        this.paperSize = paperSize;
        this.paperColor = paperColor;
        this.unit = unit;
        this.supplyCompany = supplyCompany;
        this.paperWeight = paperWeight;
        this.currentQuantity = currentQuantity;
        this.history = history;
    }


    public String getPaperID() {
        return paperID;
    }

    public void setPaperID(String paperID) {
        this.paperID = paperID;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public int getPaperWeight() {
        return paperWeight;
    }

    public void setPaperWeight(int paperWeight) {
        this.paperWeight = paperWeight;
    }

    public String getPaperColor() {
        return paperColor;
    }

    public void setPaperColor(String paperColor) {
        this.paperColor = paperColor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupplyCompany() {
        return supplyCompany;
    }

    public void setSupplyCompany(String supplyCompany) {
        this.supplyCompany = supplyCompany;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Arrays getHistory() {
        return history;
    }

    public void setHistory(Arrays history) {
        this.history = history;
    }
}

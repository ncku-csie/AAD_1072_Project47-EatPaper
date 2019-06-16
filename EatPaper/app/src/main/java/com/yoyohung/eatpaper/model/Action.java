package com.yoyohung.eatpaper.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Action {

    private String action;
    private int newIndex;
    private int newQuantity;
    private Timestamp updateTime;
    public Action() {}

    public Action(String action, int currentQuantity, int newIndex) {

        Date CurrentTime = new Date(System.currentTimeMillis()) ;

        this.action = action;
        this.newQuantity = currentQuantity;
        this.newIndex = newIndex;
        this.updateTime = new Timestamp(CurrentTime);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}

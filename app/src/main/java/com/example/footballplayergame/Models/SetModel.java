package com.example.footballplayergame.Models;

public class SetModel {

    String setName;
    public int setNum;

    public SetModel() {
    }

    public SetModel(String setName, int setNum) {
        this.setName = setName;
        this.setNum = setNum;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}

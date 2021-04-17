package com.example.myapplication.Models;

import java.io.Serializable;

public class ElementModel implements Serializable {
    //private static final long serialVersionUID = 6529685098267757690L;
    private String title;
    private boolean bought;

    public ElementModel(String id) {
        this.setTitle(id);
        setBought(false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}

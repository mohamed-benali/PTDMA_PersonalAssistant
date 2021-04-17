package com.example.myapplication.Models;

import java.util.ArrayList;
import java.util.List;

public class ListModel {
    private String title;
    private List<String> elements;

    public ListModel(String id) {
        this.title = id;
        setElements(new ArrayList<>());
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getElements() {
        return elements;
    }
    public void setElements(List<String> elements) {
        this.elements = elements;
    }
}

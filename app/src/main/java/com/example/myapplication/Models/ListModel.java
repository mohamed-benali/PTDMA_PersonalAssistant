package com.example.myapplication.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListModel implements Serializable {
    //private static final long serialVersionUID = 6529685098267757690L;
    private String title;
    private List<ElementModel> elements;

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

    public List<ElementModel> getElements() {
        return elements;
    }
    public void setElements(List<ElementModel> elements) {
        this.elements = elements;
    }

    public void setBought(String elementId) {
        for(ElementModel model : elements) {
            if (model.getTitle().equals(elementId)) model.setBought(true);
        }
    }

    public void setNotBought(String elementId) {
        for(ElementModel model : elements) {
            if (model.getTitle().equals(elementId)) model.setBought(false);
        }
    }

    public boolean contains(String elementId) {
        for(ElementModel model : elements) {
            if (model.getTitle().equals(elementId)) return true;
        }
        return false;
    }

    public void add(String id) {
        ElementModel elementModel = new ElementModel(id);
        getElements().add(elementModel);
    }

    public void remove(String id_forDelete) {
        int index = -1;
        for(int i = 0; i < getElements().size(); ++i) {
            ElementModel model = getElements().get(i);
            if (model.getTitle().equals(id_forDelete)) index = i;
        }
        if(index >= 0) getElements().remove(index);
    }
}

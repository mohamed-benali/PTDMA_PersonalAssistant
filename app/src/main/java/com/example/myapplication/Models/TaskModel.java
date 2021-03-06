package com.example.myapplication.Models;

import java.io.Serializable;

public class TaskModel implements Serializable {
    //private static final long serialVersionUID = 6529685098267757690L;
    private String title;
    private String description;
    private Boolean done;


    public TaskModel(String title, String description, Boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }
    public void setDone(Boolean done) {
        this.done = done;
    }
}

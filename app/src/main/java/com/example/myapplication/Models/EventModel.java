package com.example.myapplication.Models;

public class EventModel {
    private String title;
    private String description;
    private String year;
    private String month;
    private String day;

    private Long eventID;

    public EventModel(String title, String description, String year, String month, String day, Long eventId) {
        setTitle(title);
        setDescription(description);
        setYear(year);
        setMonth(month);
        setDay(day);
        setEventID(eventId);
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

    public String getDate() {
        return getMonth()+'/' + getDay()+'/' + getYear();
    }
    public void setDate(String date) { // DD/MM/YYYY
        String[] tmp = date.split("/");
        setYear(tmp[2]);
        setMonth(tmp[1]);
        setDay(tmp[0]);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }
}

package com.example.contextreminderapp.models;

public class Reminder {

    private String id;
    private String title;
    private String message;
    private String place;

    public Reminder() {
        // Required empty constructor for Firebase
    }

    public Reminder(String title, String message, String place) {
        this.title = title;
        this.message = message;
        this.place = place;
    }

    public Reminder(String id, String title, String message, String place) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPlace() {
        return place;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
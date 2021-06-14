package com.ptit.pthlong.Model;

public class Note {
    private String title;
    private String date;
    private String description;
    private Boolean done;
    private String id;
    public Note(){

    }

    public Note(String title, String date, String description, Boolean done) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

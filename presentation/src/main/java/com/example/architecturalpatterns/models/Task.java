package com.example.architecturalpatterns.models;

public class Task {

    private long id;
    private String title;
    private String desc;
    private boolean completed;

    public Task() {}

    public Task(long id, String title, String desc, boolean completed) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void check() {
        throw new UnsupportedOperationException("Not implemented method");
    }
}

package com.candy.model;

public class Event {
    private String title;
    private String category;
    private String startTime;
    private String endTime;
    private String description;

    // Constructor with start and end time
    public Event(String title, String category, String startTime, String endTime, String description) {
        this.title = title;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

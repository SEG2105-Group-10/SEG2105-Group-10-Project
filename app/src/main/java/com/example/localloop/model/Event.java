package com.example.localloop.model;

public class Event {
    private int id;
    private String name;
    private String description;
    private double fee;
    private int categoryId;
    private String date; // Or use long timestamp if you prefer
    private String time;
    private String organizerUsername;

    public Event() {
    }

    public Event(int id, String name, String description, double fee, int categoryId, String date, String time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fee = fee;
        this.categoryId = categoryId;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getOrganizerUsername() {
        return organizerUsername;
    }

    public void setOrganizerUsername(String organizerUsername) {
        this.organizerUsername = organizerUsername;
    }
}

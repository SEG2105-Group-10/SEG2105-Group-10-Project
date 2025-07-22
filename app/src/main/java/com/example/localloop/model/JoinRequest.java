package com.example.localloop.model;

public class JoinRequest {
    private int id;
    private int eventId;
    private String attendeeUsername;
    private String status;

    public JoinRequest() {}

    public JoinRequest(int id, int eventId, String attendeeUsername, String status) {
        this.id = id;
        this.eventId = eventId;
        this.attendeeUsername = attendeeUsername;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getAttendeeUsername() {
        return attendeeUsername;
    }

    public void setAttendeeUsername(String attendeeUsername) {
        this.attendeeUsername = attendeeUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

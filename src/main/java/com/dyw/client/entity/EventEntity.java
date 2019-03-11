package com.dyw.client.entity;

public class EventEntity {
    private String eventName;
    private int eventId;
    private int eventStatus;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }
}

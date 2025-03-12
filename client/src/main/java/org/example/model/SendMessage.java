package org.example.model;

import java.io.Serializable;

public class SendMessage implements Serializable {
    private String sender;
    private String toRoom;
    private String message;

    public SendMessage(String sender, String toRoom, String message) {
        this.sender = sender;
        this.toRoom = toRoom;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getToRoom() {
        return toRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setToRoom(String toRoom) {
        this.toRoom = toRoom;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

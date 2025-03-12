package org.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomChat implements Serializable {
    private String nameRoom;
    private String creater;
    private ArrayList<Message> messages;

    public RoomChat(String nameRoom, String creater, ArrayList<Message> messages) {
        this.nameRoom = nameRoom;
        this.creater = creater;
        this.messages = messages;
    }

    public RoomChat(String nameRoom, String creater) {
        this.nameRoom = nameRoom;
        this.creater = creater;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public String getCreater() {
        return creater;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}

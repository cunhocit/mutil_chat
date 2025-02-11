package org.example.model;

import java.io.Serializable;

public class JsonResponse implements Serializable {
    private String command;
    private Object result;
    private String clientID;

    public JsonResponse(String command, Object result, String clientID) {
        this.command = command;
        this.result = result;
        this.clientID = clientID;
    }

    public JsonResponse(String command, Object result) {
        this.command = command;
        this.result = result;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getCommand() {
        return command;
    }

    public Object getResult() {
        return result;
    }

    public String getClientID() {
        return clientID;
    }
}

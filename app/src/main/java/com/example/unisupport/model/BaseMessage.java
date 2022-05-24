package com.example.unisupport.model;

public class BaseMessage {

    int id;
    boolean fromMe;
    String message;
    String date;

    public BaseMessage(){}

    public BaseMessage(int id, boolean fromMe, String message) {
        this.id = id;
        this.fromMe = fromMe;
        this.message = message;
    }

    public BaseMessage(int id, boolean fromMe, String message, String date) {
        this.id = id;
        this.fromMe = fromMe;
        this.message = message;
        this.date = date;
    }

    public BaseMessage(boolean byMeOrByOthers, String message) {
        this.fromMe = byMeOrByOthers;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFromMe() {
        return fromMe;
    }

    public void setFromMe(boolean fromMe) {
        this.fromMe = fromMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

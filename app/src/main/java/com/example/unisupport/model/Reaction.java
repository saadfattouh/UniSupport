package com.example.unisupport.model;

public class Reaction {

    private int id;
    private int type;
    private int user_id;
    private String user_name;
    private String user_image;

    public Reaction(int id, int type, int user_id, String user_name, String user_image) {
        this.id = id;
        this.type = type;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}

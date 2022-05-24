package com.example.unisupport.model;

public class Administrative {

    private int id;
    private String name;
    private String image;

    public Administrative() {}

    public Administrative(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

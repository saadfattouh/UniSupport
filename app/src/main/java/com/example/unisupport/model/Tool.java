package com.example.unisupport.model;

public class Tool {

    private int id;
    private String name;
    private String url;
    private int topic_id;


    public Tool(){};

    public Tool(int id, String name, String url, int topic_id) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.topic_id = topic_id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }
}

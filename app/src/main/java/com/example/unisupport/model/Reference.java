package com.example.unisupport.model;

public class Reference {

    private int id;
    private String url;
    private int topic_id;

    public Reference(){}

    public Reference(int id, String url, int topic_id) {
        this.id = id;
        this.url = url;
        this.topic_id = topic_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

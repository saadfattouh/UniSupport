package com.example.unisupport.model;

public class Page {

    private int id;
    private String content;
    private String url;
    private String date;
    private int admin_id;
    private String admin_name;

    public Page(){}

    public Page(int id, String content, String url, String date, int admin_id, String admin_name) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.date = date;
        this.admin_id = admin_id;
        this.admin_name = admin_name;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}

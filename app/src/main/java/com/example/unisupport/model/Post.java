package com.example.unisupport.model;

public class Post {

    private int id;

    private String name;
    private String image;

    private String date;
    private String title;
    private String content;
    private int publisherId;

    private int reactions;

    private int tools;

    private int references;

    private int myReaction;


    public Post(){}

    public Post(int id, String name, String image, String date, String title, String content, int reactions, int tools, int references, int myReaction) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.title = title;
        this.content = content;
        this.reactions = reactions;
        this.tools = tools;
        this.references = references;
        this.myReaction = myReaction;
    }

    public Post(int id, String content) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.title = title;
        this.content = content;
        this.reactions = reactions;
        this.tools = tools;
        this.references = references;
        this.myReaction = myReaction;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReactions() {
        return reactions;
    }

    public void setReactions(int reactions) {
        this.reactions = reactions;
    }

    public int getTools() {
        return tools;
    }

    public void setTools(int tools) {
        this.tools = tools;
    }

    public int getReferences() {
        return references;
    }

    public void setReferences(int references) {
        this.references = references;
    }

    public int getMyReaction() {
        return myReaction;
    }

    public void setMyReaction(int myReaction) {
        this.myReaction = myReaction;
    }
}

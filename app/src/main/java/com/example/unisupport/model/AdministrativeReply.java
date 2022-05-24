package com.example.unisupport.model;

public class AdministrativeReply {

    private int id;
    private int admin_id;
    private String a_name;
    private String question;
    private String answer;
    private int asker_id;
    private String date;
    private int status;

    public AdministrativeReply(){}

    public AdministrativeReply(int id, String a_name, String question, String answer, int asker_id, int status) {
        this.id = id;
        this.a_name = a_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
        this.status = status;
    }



    public AdministrativeReply(int id, String a_name, String question, String answer, int asker_id) {
        this.id = id;
        this.a_name = a_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAsker_id() {
        return asker_id;
    }

    public void setAsker_id(int asker_id) {
        this.asker_id = asker_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }
}

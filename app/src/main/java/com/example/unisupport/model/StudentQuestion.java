package com.example.unisupport.model;

public class StudentQuestion {
    private int id;
    private String psychologist_id;
    private String s_name;
    private String question;
    private String answer;
    private int asker_id;
    private String date;
    private int status;

    public StudentQuestion(){
    }

    public StudentQuestion(int id, String s_name, String question, String answer, int asker_id, int status) {
        this.id = id;
        this.s_name = s_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
        this.status = status;
    }


    public StudentQuestion(int id, String s_name, String question, String answer, int asker_id) {
        this.id = id;
        this.s_name = s_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
    }

    public String getPsychologist_id() {
        return psychologist_id;
    }

    public void setPsychologist_id(String psychologist_id) {
        this.psychologist_id = psychologist_id;
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

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String a_name) {
        this.s_name = a_name;
    }
}

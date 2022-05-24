package com.example.unisupport.model;

public class PsychologistReply {

    private int id;
    private int psychologist_id;
    private String p_name;
    private String question;
    private String answer;
    private int asker_id;
    private String date;

    public PsychologistReply(){}

    public PsychologistReply(int id, int psychologist_id, String p_name, String question, String answer, int asker_id) {
        this.id = id;
        this.psychologist_id = psychologist_id;
        this.p_name = p_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
    }

    public PsychologistReply(int id, int psychologist_id, String p_name, String question, String answer, int asker_id, String date) {
        this.id = id;
        this.psychologist_id = psychologist_id;
        this.p_name = p_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
        this.date = date;
    }

    public PsychologistReply(int id, String p_name, String question, String answer, int asker_id) {
        this.id = id;
        this.p_name = p_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPsychologist_id() {
        return psychologist_id;
    }

    public void setPsychologist_id(int psychologist_id) {
        this.psychologist_id = psychologist_id;
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

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}

package com.example.unisupport.model;

public class StudentConsultation {
    private int id;
    private String admin_id;
    private String s_name;
    private String question;
    private String answer;
    private int asker_id;
    private String date;
    private int status;

    public StudentConsultation(){
    }

    public StudentConsultation(int id, String s_name, String question, String answer, int asker_id, int status) {
        this.id = id;
        this.s_name = s_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
        this.status = status;
    }



    public StudentConsultation(int id, String s_name, String question, String answer, int asker_id) {
        this.id = id;
        this.s_name = s_name;
        this.question = question;
        this.answer = answer;
        this.asker_id = asker_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
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

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String a_name) {
        this.s_name = a_name;
    }
}

package com.rizkiduwinanto.kriptografi2;

public class Message {
    private int id;
    private String from;
    private String subject;
    private String message;

    public Message(int id, String from, String subject, String message) {
        this.id = id;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

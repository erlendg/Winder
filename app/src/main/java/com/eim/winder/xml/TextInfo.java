package com.eim.winder.xml;

import java.util.Date;

/**
 * Created by Erlend on 01.02.2016.
 */
public class TextInfo {
    private String from;
    private String to;
    private String title;
    private String body;

    public TextInfo(String from, String body, String title, String to) {
        this.from = from;
        this.body = body;
        this.title = title;
        this.to = to;
    }
    public TextInfo(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

package com.eim.winder;

import java.util.Date;

/**
 * Created by Erlend on 01.02.2016.
 */
public class TextInfo {
    private Date from;
    private Date to;
    private String title;
    private String body;

    public TextInfo(Date from, String body, String title, Date to) {
        this.from = from;
        this.body = body;
        this.title = title;
        this.to = to;
    }
    public TextInfo(){

    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
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

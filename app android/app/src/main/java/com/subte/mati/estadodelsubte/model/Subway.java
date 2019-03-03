package com.subte.mati.estadodelsubte.model;

import android.graphics.Color;
import android.text.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Subway {

    private String line;
    private String state;
    private int error;
    private String updated_at;
    private String status;

    private final String status_normal = "normal";
    private final String status_closed = "closed";
    private final String status_limited = "limited";

    public Subway(String line, String state, int error, String updated_at, String status){
        this.line = line;
        this.state = state;
        this.error = error;
        this.updated_at = updated_at;
        this.status = status;
    }

    public String getLine(){
        return this.line;
    }

    public int getColor(){
        if (this.status.equals(this.status_normal)){
            return Color.GREEN;
        } else if (this.status.equals(this.status_limited)){
            return Color.rgb(239, 127, 26); //Orange
        } else {
            return Color.RED;
        }
    }

    public boolean error(){
        return this.error == 1;
    }

    public String getUpdatedDate(){
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = parser.parse(this.updated_at);
        } catch (ParseException e) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-6"));
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public String getState() {
        return Html.fromHtml(this.state, Html.FROM_HTML_MODE_LEGACY).toString();
    }
}

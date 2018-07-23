package com.subte.mati.estadodelsubte.model;

import android.graphics.Color;
import android.text.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class Subway {

    private String line;
    private String state;
    private int error;
    private String updated_at;

    public Subway(String line, String state, int error, String updated_at){
        this.line = line;
        this.state = state;
        this.error = error;
        this.updated_at = updated_at;
    }

    public String getLine(){
        return this.line;
    }

    public int getColor(){
        String newState = this.state.toLowerCase();

        if (newState.contains("normal") || newState.contains("habitual") || newState.contains("completo")){
            return Color.GREEN;
        } else if (newState.contains("limitado") || newState.contains("demora")){
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
        return Html.fromHtml(this.state).toString();
    }
}

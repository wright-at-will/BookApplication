package com.cs4743.Model;

import java.time.LocalDateTime;

public class AuditTrailEntry {

    public int id;
    public LocalDateTime dateAdded;
    public String message;

    public AuditTrailEntry() {
        id = this.getId();
        dateAdded = this.getDateAdded();
        message = this.getMessage();
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public LocalDateTime getDateAdded(){
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded){
        this.dateAdded = dateAdded;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
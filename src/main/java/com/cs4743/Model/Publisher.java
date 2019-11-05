package com.cs4743.Model;

import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;

import com.cs4743.Services.PublisherTableGateway;

public class Publisher {

    private int id;
    private SimpleStringProperty publisherName;
    public LocalDateTime date_added;

    private PublisherTableGateway gateway;

    public Publisher(String name) {
        publisherName = new SimpleStringProperty();
        setPublisherName(name);
        date_added = this.getDate_added();
    }

    public Publisher() {
        publisherName = new SimpleStringProperty();
        setPublisherName("");
        date_added = this.getDate_added();
    }

    public String getPublisherName() {
        return publisherName.get();
    }

    public void setPublisherName(String name) {
        this.publisherName.set(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimpleStringProperty nameProperty() {
        return publisherName;
    }

    public LocalDateTime getDate_added() {
        return date_added;
    }

    public void setDate_added(LocalDateTime date_added) {
        this.date_added = date_added;
    }

    @Override
    public String toString() {
        return publisherName.get();
    }
}

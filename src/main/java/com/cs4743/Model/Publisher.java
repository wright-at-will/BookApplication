package com.cs4743.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.cs4743.Services.PublisherTableGateway;

public class Publisher {

    private int id;
    private String publisherName;
    public Timestamp date_added;

    private PublisherTableGateway gateway;

    public Publisher(String publisherName, Timestamp date_added) {
        publisherName = this.publisherName;
        date_added = this.date_added;
    }


    public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate_added() {
		return date_added;
	}

	public void setDate_added(Timestamp timestamp) {
		this.date_added = timestamp;
	}
}

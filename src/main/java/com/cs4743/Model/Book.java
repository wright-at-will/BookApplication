package com.cs4743.Model;

import com.cs4743.Services.BookTableGateway;
import com.cs4743.Services.BookException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Book {

    private int bookID;
    public String title;
    public String summary;
    public int pubYear;
    public String isbn;
	public int publisherId = 1;
	public LocalDateTime lastModified = null;

    public Book(){
        this.bookID = 0;
    }

    public Book(int bookID, String title, String summary, int pubYear, String isbn){
        this.bookID = bookID;
        this.title = title;
        this.summary = summary;
        this.pubYear = pubYear;
        this.isbn = isbn;
        lastModified = this.getLastModified();
        publisherId = this.getPublisherId();
    }

    public Book(int bookID, String title){
        this.bookID = bookID;
        this.title = title;

    }

    public Book(ResultSet rs) throws SQLException {
        rs.first();
        bookID = rs.getInt("id");
        title = rs.getString("title");
        summary = rs.getString("summary");
        pubYear = rs.getInt("year_published");
        isbn = rs.getString("isbn");
    }

    //Save handles all the setters at once
	public void save() {
		if(!isValidTitle()) {
			 Alert a = new Alert(AlertType.ERROR); 
			 a.setContentText("Title must be between 1 and 255 chars");
			 a.show();
			throw new BookException("Title must be between 1 and 255 chars");
		}
		if(!isValidSummary()){
			Alert a = new Alert(AlertType.ERROR); 
			a.setContentText("Summary must be less than 65536 chars");
			a.show();
			throw new BookException("Summary must be less than 65536 chars");
		}
		if(!isValidYearPublished()){
			Alert a = new Alert(AlertType.ERROR); 
			a.setContentText("YearPublished must be between 1455 and 2019");
			a.show();
			throw new BookException("YearPublished must be between 1455 and 2019");
		}
		if(!isValidIsbn()) {
			Alert a = new Alert(AlertType.ERROR); 
			a.setContentText("ISBN must be less than 13 chars");
			a.show();
			throw new BookException("ISBN must be less than 13 chars");
		}

		BookTableGateway.getInstance().saveBook(this);
	}
	
	public List<AuditTrailEntry> getAuditTrail(){
		List<AuditTrailEntry> auditTrail = BookTableGateway.getInstance().getAuditTrail(this.getBookID());
		return auditTrail;
	}
  

	public boolean isValidTitle() {
		if(title.length() < 1 || title.length() > 255)
			return false;
		return true;
	}
	public boolean isValidSummary() {
		if(summary.length() > 65536)
			return false;
		return true;
	}
	public boolean isValidYearPublished() {
		if(pubYear < 1455 || pubYear >= 2020)
			return false;
		return true;
	}
	public boolean isValidIsbn() {
		if(isbn.length() > 13)
			return false;
		return true;
	}

    public void setYearPublished(int pubYear) { this.pubYear = pubYear; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }   
	public int getPublisherId() { return publisherId; }
	public void setPublisherId(int publisherId) { this.publisherId = publisherId; }	
	public void setTitle(String title) { this.title = title; }
	public void setSummary(String summary) { this.summary = summary; }
    public int getBookID(){ return bookID; }
	public void setBookID(int bookID) { this.bookID = bookID; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public int getPubYear() { return pubYear; }
	public LocalDateTime getLastModified() { return lastModified; }
	public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    
    @Override
    public String toString(){
        return title;
    }

}

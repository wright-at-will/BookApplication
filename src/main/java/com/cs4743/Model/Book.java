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
	public void save(int id, String title, String summary, String pubYear, String isbn) throws BookException{
		String error = (saveTitle(title) + saveSummary(summary) + saveYear(pubYear) + saveIsbn(isbn));
		if(error.length() > 0){
			alertShowAndThrow(error);
		}
		this.bookID = id;
		try {
			BookTableGateway.getInstance().saveBook(this);
		} catch (BookException e){
			alertShowAndThrow(e.getMessage());
		}
	}

	public void alertShowAndThrow(String context) throws BookException{
    	Alert a = new Alert(AlertType.ERROR);
    	a.setContentText(context);
    	//a.show();
    	a.showAndWait();
    	throw new BookException(context);
	}
	
	public List<AuditTrailEntry> getAuditTrail(){
		List<AuditTrailEntry> auditTrail = BookTableGateway.getInstance().getAuditTrail(this.getBookID());
		return auditTrail;
	}

	private String saveTitle(String title){
		if(title == null || title.equals(""))
			return "Title field cannot be empty";
		if(!isValidTitle(title))
			return "Title must be between 1 and 255 chars\n";
		this.title = title;
		return "";
	}

	private String saveSummary(String summary){
		if(summary == null || summary.equals(""))
			return "";
		if(!isValidSummary(summary))
			return "Summary must be less than 65536 chars";
		this.summary = summary;
		return "";
	}

	private String saveYear(String year){
		if(year == null || year.equals("") || year.equals("0"))
			return "";
		try{
			int pubYear = Integer.parseInt(year);
			if(isValidYearPublished(pubYear))
				this.pubYear = pubYear;
			else
				return "Year published must be between 1455 and 2019\n";
		} catch (Exception e){
			return "Year published must be numerical\n";
		}
		return "";
	}

	private String saveIsbn(String isbn){
		if(isbn == null || isbn.equals(""))
			return "";
		if(!isValidIsbn(isbn))
			return "ISBN must be less than 13 chars\n";
		this.isbn = isbn;
		return "";
	}



	public boolean isValidTitle(String title) {
		if(title.length() < 1 || title.length() > 255)
			return false;
		return true;
	}
	public boolean isValidSummary(String summary) {
		if(summary.length() > 65536)
			return false;
		return true;
	}
	public boolean isValidYearPublished(int pubYear) {
		if(pubYear < 1455 || pubYear >= 2020)
			return false;
		return true;
	}
	public boolean isValidIsbn(String isbn) {
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

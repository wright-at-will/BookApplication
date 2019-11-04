package com.cs4743.Model;

import com.cs4743.Services.BookTableGateway;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {

    private int bookID;
    public String title;
    public String summary;
    public int pubYear;
    public String isbn;

    public Book(){
        this.bookID = 0;
    }

    public Book(int bookID, String title, String summary, int pubYear, String isbn){
        this.bookID = bookID;
        this.title = title;
        this.summary = summary;
        this.pubYear = pubYear;
        this.isbn = isbn;
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

    public int getBookID(){ return bookID; }
    public String getTitle() {
        return title;
    }
    public String getSummary() {
        return summary;
    }
    public String getPubYear() {
        if(pubYear <1400) return null;
        return pubYear+"";
    }
    public String getIsbn() {
        return isbn;
    }

    //Save handles all the setters at once
    public boolean save(String title, String summary, String year, String isbn){
        if(saveTitle(title) && saveSummary(summary) && saveYear(year) && saveIsbn(isbn)) {
            bookID = BookTableGateway.update(this);
            if (bookID == 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean saveTitle(String title){
        if(title == null || title.equals(""))
            return false;
        this.title = title;
        return true;
    }
    private boolean saveSummary(String summary){
        if(summary == null || summary.equals(""))
            return true;
        if(summary.length() > 65535)
            return false;
        this.summary = summary;
        return true;
    }
    private boolean saveYear(String year){
       if(year == null || year.equals(""))
           return true;
       try{
           this.pubYear = Integer.parseInt(year);
       } catch (Exception e){
           return false;
       }
       return true;
    }
    private boolean saveIsbn(String isbn){
        if(isbn == null || isbn.equals(""))
            return true;
        if(isbn.length() > 13)
            return false;
        this.isbn = isbn;
        return true;
    }

    public void setYearPublished(int pubYear) {
        this.pubYear = pubYear;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString(){
        return title;
    }
}

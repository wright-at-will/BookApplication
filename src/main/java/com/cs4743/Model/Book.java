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

    private void setBookID(int bookID){ this.bookID = bookID; };
    public int getBookID(){ return bookID; }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        try {
            this.pubYear = Integer.parseInt(pubYear);
        } catch (Exception e){

        }
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void save(){
        System.out.println(this);
        BookTableGateway.update(this);
    }

    @Override
    public String toString(){
        return title;
    }
}

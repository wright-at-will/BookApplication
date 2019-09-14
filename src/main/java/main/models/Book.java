package main.models;

import com.sun.javafx.image.IntPixelGetter;
import main.utility.BookException;
public class Book {
	private BookDataStore bookDataStore;

	private String name;
	private String summary;
	private int yearPublished;
	private String isbn;
	
	public Book(String bookName) {
		if(!isValidName(bookName))
			throw new BookException("Invalid book name: " + bookName);
		this.name = bookName;
	}

	public static boolean isValidName(String bookName) {
		if(bookName == null)
			return false;
		if(bookName.trim().length()<1)
			return false;
		return true;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getYearPublished() {
		return Integer.toString(yearPublished);
	}

	public void setYearPublished(String yearPublished) throws Exception{
		int year = Integer.parseInt(yearPublished);
		if(year < 1900)
			throw new Exception();
		this.yearPublished = year;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Override
	public String toString() {
		return name; //return name + " " + summary +" " + yearPublished + " " + isbn;
	}
}

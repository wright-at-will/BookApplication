package main.models;

public class Book {
	private BookDataStore bookDataStore;

	private String name;
	private String summary;
	private int yearPublished;
	private String isbn;
	
	public Book(BookDataStore bookDataStore) {
		this.bookDataStore = bookDataStore;
	}

	public Book(BookDataStore bookDataStore, String name, String summary, int yearPublished, String isbn) throws Exception{
		this.bookDataStore = bookDataStore;
		this.name = name;
		this.summary = summary;
		setYearPublished(yearPublished);
		this.isbn = isbn;
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

	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) throws Exception{
		if(yearPublished < 1900)
			throw new Exception();
		this.yearPublished = yearPublished;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	
}

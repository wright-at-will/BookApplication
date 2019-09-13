package main.controllers;

import main.models.Book;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class BookDetailController implements Initializable, BookController{

	private Book book;

	@FXML
	private TextField bookName;

	@FXML
	private TextField summary;

	@FXML
	private TextField yearPublished;

	@FXML
	private TextField isbn;

	public BookDetailController(Book book){
		this.book = book;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//bookName.setText(book.getName());
	}


	public Book getBook(){
		return book;
	}

	public void setBook(Book book){
		this.book = book;
	}
}

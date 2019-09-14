package main.controllers;

import javafx.scene.control.TextArea;
import main.models.Book;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class BookDetailController implements Initializable, BookController{


    private Book book;

    @FXML
    private TextField Title;

    @FXML
    private TextArea Summary;

    @FXML
    private TextField isbn;

    @FXML
    private TextField yearPublished;
    public BookDetailController(Book book) {this.book = book;}

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Title.setText(book.getName());
        Summary.setText(book.getSummary());
        isbn.setText(book.getIsbn());
        yearPublished.setText(book.getYearPublished());
    }
}

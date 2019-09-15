package com.cs4743;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BookDetailController implements Initializable, MasterController {

    private static Logger logger = LogManager.getLogger(BookDetailController.class);

    @FXML
    private Label isbnLabel, titleLabel, summaryLabel, yearLabel;

    @FXML
    private Button saveButton;

    // get the bookListIndex and generate book details based on the index
    private BookListIndex bookListIndex = BookListController.getBookIndex();
    Book book = new Book().buildBook(bookListIndex);

    // format output for the detail view
    public void createViewDetails(Book b){
        titleLabel.setText("Title: " + b.getTitle());
        summaryLabel.setText("Summary: " +b.getSummary());
        yearLabel.setText("Publishing Year: " + Integer.toString(b.getPubYear()));
        isbnLabel.setText("ISBN: " + b.getIsbn());
    }

    @FXML
    public void clickSaveButton(ActionEvent event){
        logger.info("Save button was clicked");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createViewDetails(book);
    }


}

package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Services.BookTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookDetailController implements Initializable, MasterController {

    private static Logger logger = LogManager.getLogger(BookDetailController.class);

    //@FXML
    //private Label isbnLabel, titleLabel, summaryLabel, yearLabel;
    @FXML
    private TextField titleField, yearField, isbnField;
    @FXML
    private TextArea summaryArea;
    @FXML
    private Button saveButton;

    private Book book;

    public BookDetailController(Book book){
        this.book = book;
    }
    // format output for the detail view
    public void createViewDetails(){
        if(book != null || book.getBookID() > 0) {
            book.getTitle();
            System.out.println(book.toString());
            titleField.setText(book.getTitle());
            summaryArea.setText(book.getSummary());
            yearField.setText(book.getPubYear()+"");
            isbnField.setText(book.getIsbn());
        }
        titleField.setPromptText("Title");
        summaryArea.setPromptText("Summary");
        yearField.setPromptText("Year Published");
        isbnField.setPromptText("ISBN");
    }

    @FXML
    public void clickSaveButton(ActionEvent event){
        logger.info("Save button was clicked");
        logger.info("title: {} \nsummary: {} \nyear:  {} \nisbn: {} \n",
                titleField.getText(),
                summaryArea.getText(),
                yearField.getText(),
                isbnField.getText());
        if(!book.save(titleField.getText(),summaryArea.getText(),yearField.getText(),isbnField.getText()))
            logger.error("Save failed");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createViewDetails();
    }



}

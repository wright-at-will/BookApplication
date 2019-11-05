package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Model.Publisher;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.View.ViewType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookDetailController implements Initializable, MasterController {

    private static Logger logger = LogManager.getLogger(BookDetailController.class);

    @FXML
    private TextField titleField, yearField, isbnField;
    @FXML
    private TextArea summaryArea;
    @FXML
    private Button saveButton, editButton;

    @FXML
    private Button auditTrailButton;

    @FXML
    private ComboBox<String> publisherComboBox;

    private Book book;

    public static boolean verifyUserSaved = false;

    //ObservableList<Publisher> trackPublisher = FXCollections.observableArrayList();
    //ObservableList<String> publisherNameList = FXCollections.observableArrayList();

    BookDetailController() {}

    public BookDetailController(Book book){
        this.book = book;
    }

    // format output for the detail view
    public void createViewDetails(){
        auditTrailButton.setDisable(true);
        if(book != null && book.getBookID() > 0) {
            auditTrailButton.setDisable(false);
            titleField.setText(book.getTitle());
            summaryArea.setText(book.getSummary());
            yearField.setText(Integer.toString(book.getPubYear()));
            isbnField.setText(book.getIsbn());

      //      for (int i = 0; i < trackPublisher.size(); i++) {
      //          if (trackPublisher.get(i).getId() == book.getPublisherId()) {
       //             publisherComboBox.setValue(trackPublisher.get(i).getPublisherName());
       //             break;
       //         }
            //    }
        }
        titleField.setPromptText("Title");
        summaryArea.setPromptText("Summary");
        yearField.setPromptText("Year Published");
        isbnField.setPromptText("ISBN");
        //publisherComboBox.setValue(publisherNameList.get(1));
    }

    @FXML
    public void clickedSaveButton(ActionEvent event) throws SQLException {
        if (book.getBookID() == 0) {
            Book newBook = new Book();
            newBook.setTitle(summaryArea.getText());
            newBook.setSummary(summaryArea.getText());
            try{
                newBook.setYearPublished(Integer.parseInt(yearField.getText()));
            } catch (NumberFormatException err){
                logger.info("Empty String could not be converted to int. Replaced year published with default 1455");
                newBook.setYearPublished(1455);
            }
            newBook.setIsbn(isbnField.getText());
            /*for (int i = 0; i < trackPublisher.size(); i++){
                if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
                    newBook.setPublisherId(trackPublisher.get(i).getId());
                    break;
                }
            }*/
            newBook.save();
            addAuditInfoNewBook(newBook.getBookID());
            logger.info("Save button was clicked");
        }
        // book exists
        else {
            if(!book.getTitle().equals(titleField.getText())) {
                addAuditInfoUpdateBook(book.getBookID() ,"Title", book.getTitle(), titleField.getText());
            }
            if(!book.getSummary().equals(summaryArea.getText())) {
                addAuditInfoUpdateBook(book.getBookID() ,"Summary", book.getSummary(), summaryArea.getText());
            }
            if(book.getPubYear() != (Integer.parseInt(yearField.getText()))) {
                addAuditInfoUpdateBookInteger(book.getBookID() ,"Year Published", book.getPubYear(), Integer.parseInt(yearField.getText()));
            }
            if(!book.getIsbn().equals(isbnField.getText())) {
                addAuditInfoUpdateBook(book.getBookID() ,"Isbn", book.getIsbn(), isbnField.getText());
            }

            book.setBookID(book.getBookID());
            book.setTitle(titleField.getText());
            book.setSummary(summaryArea.getText());
            book.setYearPublished(Integer.parseInt(yearField.getText()));
            book.setIsbn(isbnField.getText());
            //for (int i = 0; i < trackPublisher.size(); i++){
            //    if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
            //        book.setPublisherId(trackPublisher.get(i).getId());
            //        break;
            //    }
            //}
            book.save();
            logger.info("Save button was clicked");
        }

        MenuController.getInstance().switchView(ViewType.BOOKLISTVIEW);
    }

    @FXML
    void clickedAuditTrailButton(ActionEvent event) {
        logger.info("Clicked audit trail button");
        /*
        Optional<ButtonType> result = MenuController.alert.showAndWait();
        if(event.getSource() == auditTrailButton && BookListController.bdc.checkbook()
             && (BookListController.bdc.checkForChanges() || BookListController.bdc.checkForChangesNewBook())) {
                logger.info("Selected auditTrailButton");

                if(result.get() == MenuController.yes){
                    logger.info("Yes was pressed");
                    BookListController.bdc.fireSave();
                    result = switchAuditTrailView();
                }
                else if (result.get() == MenuController.no){
                    logger.info("No was pressed");
                    result = switchAuditTrailView();
                }
                else if (result.get() == MenuController.cancel)
                    logger.info("Cancel was pressed");
                } else {
	                logger.info("No changes found");
                    result = switchAuditTrailView();
                }   
            MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
        */
        }

    // launch save button
    public void fireSave(){
        saveButton.fire();
    }
    
	public Boolean checkSelection() {
		if(this.book.getBookID() == 0) {
			return false;
		} else {
			return true;
		}
	}
/*
    // updates the list of publishers on the detail view from the database list
    public void updatePubisherLists(List<Publisher> publisher){
        trackPublisher.clear();
        for(int i=0; i< publisher.size(); i++){
            publisherNameList.add(publisher.get(i).getPublisherName());
            trackPublisher.add(publisher.get(i));
        }
    }
*/
    // adds audit info for a new book
    public void addAuditInfoNewBook(int bookID) throws SQLException{
        logger.info(bookID);

        BookTableGateway.insertAuditTrailEntry(bookID, "Book added");
    }

    // adds audit info for an updated book string
    public void addAuditInfoUpdateBook(int bookID, String field, String previousValue, String newValue) throws SQLException{
        String message = field + " changed from \"" + previousValue + "\" to \"" + newValue + "\"";

        BookTableGateway.insertAuditTrailEntry(bookID, message);
    }

    // adds audit info for an updated book int
    public void addAuditInfoUpdateBookInteger(int bookID, String field, int previousValue, int newValue) throws SQLException{
        String message = field + " changed from \"" + previousValue + "\" to \"" + newValue + "\"";

        BookTableGateway.insertAuditTrailEntry(bookID, message);
    }

    // search for differences between the text fields and what they previously were
    public Boolean checkForChanges() {
        if(book.getTitle().equals(titleField.getText()) &&
                book.getSummary().equals(summaryArea.getText()) &&
                Integer.toString(book.getPubYear()) == (yearField.getText()) &&
                book.getIsbn().equals(isbnField.getText())) {
            return false;
        } else {
            return true;
        }

    }

    // search for differences between the text fields and what they should be by default
    public Boolean checkForChangesNewBook() {
        logger.info(titleField.getText());
        if(titleField.getText().equals("") && summaryArea.getText().equals("") &&
                yearField.getText().equals("") && isbnField.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // check to see if the selection is a new book
    public Boolean checkbook() {
        if(this.book.getBookID() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Optional<ButtonType> switchAuditTrailView() {
        MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
        BookDetailController.verifyUserSaved = false;
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createViewDetails();
    }

}

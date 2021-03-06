package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Model.Publisher;
import com.cs4743.Services.BookException;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.Services.PublisherTableGateway;
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
    
    private BookTableGateway btg;
     
    @FXML
    private TextField titleField, yearField, isbnField;
    @FXML
    private TextArea summaryArea;
    @FXML
    private Button saveButton, editButton, auditTrailButton;
    @FXML
    private ComboBox<Publisher> publisherComboBox;

    private Book book, tempBook;

    public static boolean verifyUserSaved = false;

    ObservableList<Publisher> trackPublisher = FXCollections.observableArrayList();
    //ObservableList<String> publisherNameList = FXCollections.observableArrayList();

    BookDetailController() {}

    public BookDetailController(Book book, BookTableGateway btg){
        this.book = book;
        this.btg = btg;
    }

    // format output for the detail view
    public void createViewDetails(){
        System.out.println("===Inside BookDetailController: createViewDetails ===\nBook ID: " + book.getBookID() + "\nBook Title: " + book.getTitle() + 
        		"\nBook Year: " + book.getPubYear() + "\nBook ISBN: " + book.getIsbn() + "\nBook Summary: " + book.getSummary());
        auditTrailButton.setDisable(true);
        if(book != null && book.getBookID() > 0) {
            auditTrailButton.setDisable(false);
            titleField.setText(book.getTitle());
            summaryArea.setText(book.getSummary());
            yearField.setText(book.pubYear>0?Integer.toString(book.getPubYear()):"");
            isbnField.setText(book.getIsbn());

            publisherComboBox.setItems(trackPublisher);
            try {
                publisherComboBox.getSelectionModel().select(book.getPublisher());
                //publisherComboBox.setValue(publisherComboBox);
            } catch (Exception e){

            }
        }
        titleField.setPromptText("Title");
        summaryArea.setPromptText("Summary");
        yearField.setPromptText("Year Published");
        isbnField.setPromptText("ISBN");

        //publisherComboBox.setValue(trackPublisher.get(0));
        //
        // publisherComboBox.setValue(publisherNameList.get(0));
    }

    @FXML
    public void clickedSaveButton(ActionEvent event) throws SQLException {
        if (book.getBookID() == 0) {
            Book newBook = new Book();
            //newBook.saveTitle(titleField.getText());
            //newBook.saveSummary(summaryArea.getText());
            //try{
            //    newBook.saveYear(yearField.getText());
            //} catch (NumberFormatException err){
            //    logger.info("Empty String could not be converted to int. Replaced year published with default 1455");
            //    newBook.saveYear("1455");
           // }
            //newBook.saveIsbn(isbnField.getText());
            //newBook.setPublisherId(publisherComboBox.getValue().getId());
            //newBook.setPublisher(publisherComboBox.getItems().get(publisherComboBox.getSelectionModel().getSelectedIndex()));
            newBook.save(0,titleField.getText(),summaryArea.getText(),yearField.getText(),isbnField.getText(),publisherComboBox.getItems().get(publisherComboBox.getSelectionModel().getSelectedIndex()));
            logger.info("Save button was clicked");
        } else if (book.getBookID() > 0) {

            logger.info("Save button was clicked and book exists");
            
            if(checkTitle()) {
                addAuditInfoUpdateBook(book.getBookID() ,"title", book.getTitle(), titleField.getText());
                book.saveTitle(titleField.getText());
            }
            if(checkSummary()) {
                addAuditInfoUpdateBook(book.getBookID() ,"summary", book.getSummary(), summaryArea.getText());
                book.saveSummary(summaryArea.getText());
            }
            if(checkPubYear()) {
                addAuditInfoUpdateBookInteger(book.getBookID() ,"year_published", book.getPubYear(), Integer.parseInt(yearField.getText()));
                book.saveYear(yearField.getText());
            }
            if(checkIsbn()) {
                addAuditInfoUpdateBook(book.getBookID() ,"isbn", book.getIsbn(), isbnField.getText());
                book.saveIsbn(isbnField.getText());
            }
            
            //publisherComboBox.setValue(trackPublisher.get(0));
        	//for (int i = 0; i < trackPublisher.size(); i++){
        	//	if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
        	//		book.setPublisher(trackPublisher.get(i));
            //        logger.info("Entered edit publisher");
        	//		break;
        	//	}
            logger.info("Value is: " + publisherComboBox.getSelectionModel().getSelectedItem());
        	//}
            try {
                book.save(book.getBookID(), titleField.getText(), summaryArea.getText(), yearField.getText(), isbnField.getText(), publisherComboBox.getItems().get(publisherComboBox.getSelectionModel().getSelectedIndex()));
            } catch (BookException e){
                return;
            }
            
            logger.info("Save button was clicked");
        } 

        MenuController.getInstance().switchView(ViewType.BOOKLISTVIEW);
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

    // updates the list of publishers on the detail view from the database list
    public void updatePubisherLists(List<Publisher> publisher){
        trackPublisher.clear();
        trackPublisher.setAll(publisher);
        //for(int i = 0; i < publisher.size(); i++){
        //    publisherNameList.add(publisher.get(i).getPublisherName());
        //    trackPublisher.add(publisher.get(i));
        //}
    }



    // adds audit info for an updated book string
    public void addAuditInfoUpdateBook(int bookID, String field, String previousValue, String newValue) throws SQLException {
        String message = field + " changed from \"" + previousValue + "\" to \"" + newValue + "\"";

        btg.insertAuditTrailEntry(bookID, message);
    }

    // adds audit info for an updated book int
    public void addAuditInfoUpdateBookInteger(int bookID, String field, int previousValue, int newValue) throws SQLException {
        String message = field + " changed from \"" + previousValue + "\" to \"" + newValue + "\"";

        btg.insertAuditTrailEntry(bookID, message);
    }

    // search for differences between the text fields and what they previously were
    public Boolean checkForChanges() {
        if(book.getBookID() == 0)
            return checkForChangesNewBook();
        logger.info(""+checkTitle()+checkSummary()+checkPubYear()+checkIsbn());
        return checkTitle() || checkSummary() || checkPubYear() || checkIsbn();

    }
  
    private boolean checkTitle(){
        return check(book.getTitle(),titleField.getText());
    }
    private boolean checkSummary(){
        return check(book.getSummary(),summaryArea.getText());
    }

    private boolean check(String book, String gui){
        if(book != null && gui != null)
            return !book.equals(gui);
        else if(book == null && gui == null)
            return false;
        return true;
    }
    private boolean checkPubYear() {
        try {
            return book.getPubYear() != Integer.parseInt(yearField.getText());

        } catch (Exception e){
            if(yearField.getText().length() > 0)
                return true;
            if(book.getPubYear() == 0)
                return false;
            return true;
        }
    }
    private boolean checkIsbn(){
        return check(book.getIsbn(),isbnField.getText());
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
    

    //Tests if the book has been changed, returns false if not
    public Boolean checks(){
        return checkForChanges();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	List<Publisher> publishers = PublisherTableGateway.getInstance().fetchPublishers();
    	updatePubisherLists(publishers);
    	publisherComboBox.setEditable(true);
    	publisherComboBox.setItems(trackPublisher);
    	publisherComboBox.setValue(publishers.get(0));
        createViewDetails();
    }
    
    @FXML
    void clickedAuditTrailButton(ActionEvent event) {
        MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW); 
    	createTemp();
    }
    
    public void createTemp() {
        logger.info("Clicked audit trail button");
        tempBook = new Book();
        tempBook.setBookID(book.getBookID());
        tempBook.saveTitle(titleField.getText());
        tempBook.saveSummary(summaryArea.getText());  
        tempBook.saveYear(yearField.getText());
        tempBook.saveIsbn(isbnField.getText());
        publisherComboBox.setValue(trackPublisher.get(0));
        logger.info("Temp Book Created");  
        AuditTrailController.moveTempToAudit(tempBook);
    }
    
    public void restoreBook(Book book) {
        //auditTrailButton.setDisable(false);
        titleField.setText(book.getTitle());
        summaryArea.setText(book.getSummary());
        yearField.setText(book.pubYear>0?Integer.toString(book.getPubYear()):"");
        isbnField.setText(book.getIsbn());
            logger.info("Temp Book Destroyed");
            System.out.println("===Inside BookDetailController: restoreBook ===\nBook ID: " + book.getBookID() + "\nBook Title: " + book.getTitle() +
            		"\nBook Year: " + book.getPubYear() + "\nBook ISBN: " + book.getIsbn() + "\nBook Summary: " + book.getSummary());
    }

    public void print(){
        System.out.printf("%s\n%s\n%s\n%s\n", titleField.getText(), summaryArea.getText(), yearField.getText(), isbnField.getText());
    }

}

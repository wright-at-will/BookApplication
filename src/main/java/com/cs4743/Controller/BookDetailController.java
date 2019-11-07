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
    private Button saveButton, editButton;

    @FXML
    private Button auditTrailButton;

    @FXML
    private ComboBox<Publisher> publisherComboBox;

    private Book book;

    public static boolean verifyUserSaved = false;

    ObservableList<Publisher> trackPublisher = FXCollections.observableArrayList();
    ObservableList<String> publisherNameList = FXCollections.observableArrayList();

    BookDetailController() {}

    public BookDetailController(Book book, BookTableGateway btg){
        this.book = book;
        this.btg = btg;
    }

    // format output for the detail view
    public void createViewDetails(){
        auditTrailButton.setDisable(true);
        if(book != null && book.getBookID() > 0) {
            auditTrailButton.setDisable(false);
            titleField.setText(book.getTitle());
            summaryArea.setText(book.getSummary());
            yearField.setText(book.pubYear>0?Integer.toString(book.getPubYear()):"");
            isbnField.setText(book.getIsbn());

            //for (int i = 0; i < trackPublisher.size(); i++) {
            //    if (trackPublisher.get(i).getId() == book.getPublisherId()) {
            //        publisherComboBox.setValue(trackPublisher.get(i).getPublisherName());
            //        break;
            //        }
            //    }
            publisherComboBox.setItems(trackPublisher);
        }
        titleField.setPromptText("Title");
        summaryArea.setPromptText("Summary");
        yearField.setPromptText("Year Published");
        isbnField.setPromptText("ISBN");

        publisherComboBox.setValue(trackPublisher.get(0));
        //
        // publisherComboBox.setValue(publisherNameList.get(0));
    }

    @FXML
    public void clickedSaveButton(ActionEvent event) throws SQLException {
        if (book.getBookID() == 0) {
            Book newBook = new Book();
            newBook.saveTitle(summaryArea.getText());
            newBook.saveSummary(summaryArea.getText());
            try{
                newBook.saveYear(yearField.getText());
            } catch (NumberFormatException err){
                logger.info("Empty String could not be converted to int. Replaced year published with default 1455");
                newBook.saveYear("1455");
            }
            newBook.saveIsbn(isbnField.getText());
            newBook.setPublisherId(publisherComboBox.getValue().getId());

            addAuditInfoNewBook(newBook.getBookID());
            logger.info("Save button was clicked");
        } else {

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
            
            publisherComboBox.setValue(trackPublisher.get(0));
        	for (int i = 0; i < trackPublisher.size(); i++){
        		if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
        			book.setPublisherId(trackPublisher.get(i).getId());
                    logger.info("Entered edit publisher");
        			break;
        		}
        	}
            try {
                book.save(book.getBookID(), titleField.getText(), summaryArea.getText(), yearField.getText(), isbnField.getText());
            } catch (BookException e){
                return;
            }
            




            logger.info("Save button was clicked");
        }

        MenuController.getInstance().switchView(ViewType.BOOKLISTVIEW);
    }

    @FXML
    void clickedAuditTrailButton(ActionEvent event) {
        logger.info("Clicked audit trail button");
       // Optional<ButtonType> result = MenuController.alert.showAndWait();
        
        MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW); 
        /*
        if(event.getSource() == auditTrailButton && BookListController.bdc.checks()
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
                }   */
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

    // adds audit info for a new book
    public void addAuditInfoNewBook(int bookID) throws SQLException{
        logger.info(bookID);

        btg.insertAuditTrailEntry(bookID, "Book added");
    }

    // adds audit info for an updated book string
    public void addAuditInfoUpdateBook(int bookID, String field, String previousValue, String newValue) throws SQLException{
        String message = field + " changed from \"" + previousValue + "\" to \"" + newValue + "\"";

        btg.insertAuditTrailEntry(bookID, message);
    }

    // adds audit info for an updated book int
    public void addAuditInfoUpdateBookInteger(int bookID, String field, int previousValue, int newValue) throws SQLException{
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
    public Optional<ButtonType> switchAuditTrailView() {
        MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
        BookDetailController.verifyUserSaved = false;
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	List<Publisher> publishers = PublisherTableGateway.getInstance().fetchPublishers();
    	updatePubisherLists(publishers);
    	publisherComboBox.setEditable(true);
    	publisherComboBox.setItems(trackPublisher);
        createViewDetails();
    }

}

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

    ObservableList<Publisher> trackPublisher = FXCollections.observableArrayList();
    ObservableList<String> publisherNameList = FXCollections.observableArrayList();

    BookDetailController() {}

    public BookDetailController(Book book){
        this.book = book;
    }

    // format output for the detail view
    public void createViewDetails(){
        if(book != null && book.getBookID() > 0) {
            titleField.setText(book.getTitle());
            summaryArea.setText(book.getSummary());
            yearField.setText(book.getPubYear());
            isbnField.setText(book.getIsbn());

            for (int i = 0; i < trackPublisher.size(); i++) {
                if (trackPublisher.get(i).getId() == book.getPublisherId()) {
                    publisherComboBox.setValue(trackPublisher.get(i).getPublisherName());
                    break;
                }
            }
        }
        titleField.setPromptText("Title");
        summaryArea.setPromptText("Summary");
        yearField.setPromptText("Year Published");
        isbnField.setPromptText("ISBN");
        publisherComboBox.setValue(trackPublisher.get(0));
        auditTrailButton.setDisable(true);
    }

    @FXML
    public void clickSaveButton(ActionEvent event) throws SQLException {
        if (book.getId() == 0) {
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
            for (i = 0; i < trackPublisher.size(); i++){
                if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
                    newBook.setPublisherId(trackPublisher.get(i).getId());
                    break;
                }
            }
            newBook.save();
            addAuditInfoNewBook(newBook.getId());
            logger.info("Save button was clicked");
        }
        // book exists
        else {
            if(!book.getTitle().equals(titleTextField.getText())) {
                addAuditInfoUpdateBook(book.getId() ,"Title", book.getTitle(), titleTextField.getText());
            }
            if(!book.getSummary().equals(summaryTextField.getText())) {
                addAuditInfoUpdateBook(book.getId() ,"Summary", book.getSummary(), summaryArea.getText());
            }
            if(book.getYearPublished() != (Integer.parseInt(yearPublishedTextField.getText()))) {
                addAuditInfoUpdateBookInteger(book.getId() ,"Year Published", book.getYearPublished(), Integer.parseInt(yearPublishedTextField.getText()));
            }
            if(!book.getIsbn().equals(isbnTextField.getText())) {
                addAuditInfoUpdateBook(book.getId() ,"Isbn", book.getIsbn(), isbnTextField.getText());
            }

            book.setId(book.getId());
            book.setTitle(titleField.getText());
            book.setSummary(summaryArea.getText());
            book.setYearPublished(Integer.parseInt(yearField.getText()));
            book.setIsbn(isbnField.getText());
            for (i = 0; i < trackPublisher.size(); i++){
                if(trackPublisher.get(i).getPublisherName().equals(publisherComboBox.getValue())){
                    book.setPublisherId(trackPublisher.get(i).getId());
                    break;
                }
            }
            book.save();
            logger.info("Save button was clicked");
        }

        MenuController.getInstance().switchView(ViewType.BOOKLISTVIEW);
    }

    @FXML
    void clickedAuditTrail(ActionEvent event) {
        if(event.getSource() == auditTrailButton){
            logger.info("Selected auditTrailButton");
            if (MenuController.previousScene.equals("/View/BookDetailView.fxml") && BookListController.detailController.checkbook()
             && BookListController.detailController.checkForChanges()) {
                logger.info("Check Changes existing book (addbookview clicked)");
                Optional<ButtonType> result = MenuController.alert.showAndWait();
                if(result.get() == MenuController.yes){
                    logger.info("Yes was pressed");
                    BookListController.detailController.fireSave();
                    switchAuditTrailView();
                }
                else if (result.get() == MenuController.no){
                    logger.info("No was pressed");
                    switchAuditTrailView();
                }
                else if (result.get() == MenuController.cancel)
                    logger.info("Cancel was pressed");
            }
            else {
                logger.info("No changes found");
                MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
                BookDetailController.verifySave = false;
            }
        }
        else {
             if(BookListController.detailController.checkForChangesNewBook()) {
                 logger.info("Check Changes new book (auditTrailButton clicked)");
                 Optional<ButtonType> result = MenuController.alert.showAndWait();
                 if (result.get() == MenuController.yes) {
                     logger.info("Yes was pressed");
                     BookListController.detailController.fireSave();
                     switchAuditTrailView();
                 }
                 // if the user clicks no, forego saving and navigate the user to their destination
                 else if (result.get() == MenuController.no) {
                     logger.info("No was pressed");
                     switchAuditTrailView();
                 }
                 // if the user hits cancel, close the alert and return them to their screen
                 else if (result.get() == MenuController.cancel)
                     logger.info("Cancel was pressed");
             }
             // no changes were made to the new book
             else {
                 logger.info("No changes found on new book model");
                 MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
                 BookDetailController.verifySave = false;
             }
        }
        // Scene did not have save functionality. Program continues as normal
        else {
            logger.info("Previous page was not able to accept changes");
            MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
        }
            new MasterController();
            MasterController controller = MenuController.getInstance(0);
            controller.switchView(ViewType.AUDITTRAILVIEW);
        }

    // launch save button
    public void fireSave(){
        saveButton.fire();
    }

    // updates the list of publishers on the detail view from the database list
    public void updatePubisherLists(List<Publisher> publisher){
        trackPublisher.clear();
        for(int i=0; i< publisher.size(); i++){
            publisherNameList.add(publisher.get(i).getPublisherName());
            trackPublisher.add(publisher.get(i));
        }
    }

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
                Integer.toString(book.getYearPublished()).equals(yearField.getText()) &&
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
        if(this.book.getId() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void switchAuditTrailView() {
        MenuController.getInstance().switchView(ViewType.AUDITTRAILVIEW);
        BookDetailController.verifySave = false;
        result = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createViewDetails();
    }

}

package com.cs4743.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cs4743.Model.AuditTrailEntry;
import com.cs4743.Model.Book;
import com.cs4743.View.ViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class AuditTrailController implements Initializable, MasterController {

    private static Logger log = LogManager.getLogger(BookListController.class);

    public static ObservableList<AuditTrailEntry> trackChanges = FXCollections.observableArrayList();
    
    private BookDetailController bdc;
    
    private static Book book;

    @FXML
    private ListView<AuditTrailEntry> auditTrailList;

    @FXML
    private Button backToDetailsButton;

    @FXML
    private Label bookTitleLabel;

    @FXML
    void clickedBackToDetails(ActionEvent event) { 
    	//BookDetailController.restoreBook(book);
    	MenuController controller = MenuController.getInstance();
    	controller.switchView(ViewType.BOOKDETAILVIEW);
    	controller.getController().restoreBook(book);
    }

    AuditTrailController() { }

    AuditTrailController(List<AuditTrailEntry> auditTrail){
        trackChanges.clear();
        for(int i = 0; i < auditTrail.size(); i++){
            trackChanges.add(auditTrail.get(i));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String bookTitle = BookListController.getSelection().getTitle();
        bookTitleLabel.setText(bookTitle);
        auditTrailList.setEditable(true);
        log.info(trackChanges);
        auditTrailList.setItems(trackChanges);
        auditTrailList.setCellFactory(param -> new ListCell<AuditTrailEntry>() {
            @Override
            protected void updateItem(AuditTrailEntry item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getMessage() == null) {
                    setText(null);
                } else {
                    setText(item.getDateAdded().toString() + " : " + item.getMessage());
                }
            }
        });

    }
    
    public static void moveTempToAudit(Book tempBook) {
        log.info("Moved temp to audit");
        book = new Book();
		book.setBookID(tempBook.getBookID());
        book.saveTitle(tempBook.getTitle());
        book.saveSummary(tempBook.getSummary());  
        book.saveYear(String.valueOf(tempBook.getPubYear()));
        book.saveIsbn(tempBook.getIsbn());
        tempBook = null;
        System.out.println("===Inside AuditTrailController ===\nBook ID: " + book.getBookID() + "\nBook Title: " + book.getTitle() + 
        		"\nBook Year: " + book.getPubYear() + "\nBook ISBN: " + book.getIsbn() + "\nBook Summary: " + book.getSummary());
        
    }


}
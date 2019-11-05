package com.cs4743.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cs4743.Model.AuditTrailEntry;
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

    @FXML
    private ListView<AuditTrailEntry> auditTrailListLog;

    @FXML
    private Button backToDetailsButton;

    @FXML
    private Label bookTitleLabel;

    @FXML
    void clickedBackToDetails(ActionEvent event) {
        MenuController.getInstance().switchView(ViewType.BOOKDETAILVIEW);
    }

    AuditTrailController() { }

    AuditTrailController(List<AuditTrailEntry> auditTrail){
        trackChanges.clear();
        for(int i=0; i<auditTrail.size(); i++){
            trackChanges.add(auditTrail.get(i));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String bookTitle = BookListController.getSelection().getTitle();
        bookTitleLabel.setText(bookTitle);
        auditTrailListLog.setEditable(true);
        log.info(trackChanges);
        auditTrailListLog.setItems(trackChanges);
        auditTrailListLog.setCellFactory(param -> new ListCell<AuditTrailEntry>() {
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


}
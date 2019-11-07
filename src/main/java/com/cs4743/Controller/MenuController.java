package com.cs4743.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.cs4743.Model.AuditTrailEntry;
import com.cs4743.Model.Book;
import com.cs4743.Services.BookException;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.View.ViewType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuController implements Initializable {

    // define MenuController instance for the Singleton class
    private static MenuController instance = null;
    public static BookTableGateway btg = BookTableGateway.getInstance();
    private MasterController controller = null;

    // log4j logger definition
    private static Logger logger = LogManager.getLogger(MenuController.class);
    private Book book;

    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuItem showBookListMenuItem;
    @FXML
    private MenuItem closeAppMenuItem;
    @FXML
    private MenuItem newBookMenuItem;
    @FXML
    private Button auditTrailButton;

    public static ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    public static ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
    public static ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    public static Alert alert = createAlert();

    private MenuController(){ }

    /*
     * MenuController uses the singleton pattern to allow all views to access the
     * menu, located in a central location.
     */
    public static MenuController getInstance(){
        if(instance == null)
            instance = new MenuController();
        return instance;
    }

    /*
    Received action from menu item
    Check what screen we are on first and log that
    */
    @FXML
    void clickMenuItem(ActionEvent event){
        logger.info(controller);
        logger.info(event.getSource());
        if(controller instanceof BookDetailController){
            if(!cleanup((BookDetailController) controller))
                return;
        }
        if(event.getSource()==closeAppMenuItem){
                Platform.exit();
        } else if(event.getSource() == showBookListMenuItem){
            switchView(ViewType.BOOKLISTVIEW);
        } else if(event.getSource() == newBookMenuItem){
            switchView(ViewType.NEWBOOKVIEW);
        }
    }

    private boolean cleanup(BookDetailController controller){
        if(!controller.checkForChanges())
            return  stopBookTableGateway();
        Optional<ButtonType> saveMenuResult = alert.showAndWait();
        if (saveMenuResult.get() == yes) {
            logger.info("Yes was pressed");
            controller.fireSave();
        } else if (saveMenuResult.get() == no) {
            logger.info("No was pressed");
        } else if (saveMenuResult.get() == cancel) {
            logger.info("Cancel was pressed");
            return false;
        }else {
            logger.info("No changes on the model were found");
        }
        return stopBookTableGateway();
    }

    private void cleanup2() {
        Optional<ButtonType> saveMenuResult = alert.showAndWait();
        if (saveMenuResult.get() == yes) {
            logger.info("Yes was pressed");
            BookListController.bdc.fireSave();
            saveMenuResult = switchBookListView();
        } else if (saveMenuResult.get() == no) {
            logger.info("No was pressed");
            saveMenuResult = switchBookListView();
        } else if (saveMenuResult.get() == cancel)
            logger.info("Cancel was pressed");
        else {
            logger.info("No changes to the model were found");
            saveMenuResult = switchBookListView();

        }
    }

    private void cleanup3(){
        logger.info("New Book menu item clicked");

        if (BookListController.bdc == null) {
            logger.info("Book Detail Controller is null");
            List<Book> books = BookTableGateway.getInstance().bookList();
            BookListController.setSelected(books.get(0));
            switchView(ViewType.BOOKDETAILVIEW);
        }
        Optional<ButtonType> saveMenuResult = alert.showAndWait();

        if (saveMenuResult.get() == yes) {
            logger.info("Yes was pressed");
            BookListController.bdc.fireSave();
            saveMenuResult = addBookView();
        } else if (saveMenuResult.get() == no) {
            logger.info("No was pressed");
            saveMenuResult = addBookView();
        } else if (saveMenuResult.get() == cancel)
            logger.info("Cancel was pressed");
        else {
            logger.info("No changes to the model were found");
            saveMenuResult = addBookView();
        }
    }

    private Boolean stopBookTableGateway() {
        try {
            btg.closeConnection();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return true;
    }

    private Optional<ButtonType> switchBookListView() {
        switchView(ViewType.BOOKLISTVIEW);
        BookDetailController.verifyUserSaved = false;
        return null;
    }

    private Optional<ButtonType> addBookView() {
        switchView(ViewType.NEWBOOKVIEW);
        BookDetailController.verifyUserSaved = false;
        return null;
    }

    public void setDetailView(Book book){
        //this.bookID = bookID;
        this.book = book;
        logger.info("Switching to detail view of book");
        switchView(ViewType.BOOKDETAILVIEW);
    }

    public void switchView(ViewType viewType){
        String view = "";
        //MasterController controller = null;
        switch(viewType){
            case BOOKLISTVIEW:
                view = "BookListView.fxml";
                List<Book> books = BookTableGateway.getInstance().bookList();
                controller = BookListController.getBookListController(books, btg);
                logger.info("Switching to BookListView");
                break;
            case BOOKDETAILVIEW:
                view = "BookDetailView.fxml";
                try {
                    Book readBook = btg.read(book.getBookID());

                    controller = new BookDetailController(readBook, btg);
                    logger.info("Switching to BookDetailView");
                } catch (BookException e){ book.alertShowAndThrow(e.getMessage() +
                        "\n Another user may be editing this book"); }
                break;
            case NEWBOOKVIEW:
                view = "BookDetailView.fxml";
                controller = new BookDetailController(new Book(), btg);
                break;
            case SAVEDETAILCHANGES:
                view = "SaveDetailChanges.fxml";
                controller = new SaveDetailChangesController();
                break;
            case AUDITTRAILVIEW:
                view = "AuditTrailView.fxml";
                List<AuditTrailEntry> auditTrail = BookListController.getSelection().getAuditTrail();
                controller = new AuditTrailController(auditTrail);
                logger.info("Switching to AuditTrailView");
                break;
        }
        try {
            loadScreen(view, controller);
        } catch (IOException e){
            e.printStackTrace();
            logger.error("IOException");
        }
    }

    public BookDetailController getController(){
        return (BookDetailController) controller;
    }

    private void loadScreen(String view, MasterController controller) throws IOException {
        URL url = this.getClass().getClassLoader().getResource("com/cs4743/View/" + view);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        Parent viewNode = loader.load();
        borderPane.setCenter(viewNode);
    }

    public static Alert createAlert() {

        Alert alert = new Alert(AlertType.NONE, "Sample", yes, no, cancel);
        alert.setTitle("Warning: Attempting to exit page without saving changes.");
        alert.setContentText("Would you like to save your work?");
        return alert;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void fireCloseMenu() { closeAppMenuItem.fire(); }

    public void fireBookListMenu() { showBookListMenuItem.fire(); }

}

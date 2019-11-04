package com.cs4743.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.cs4743.Model.Book;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.View.ViewType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuController implements Initializable {

    // define MenuController instance for the Singleton class
    private static MenuController instance = null;

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

    //alert
    public static Alert alert = createAlert();
    public static ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    public static ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
    public static ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);


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

    @FXML
    void clickMenuItem(ActionEvent event){
        book = new Book();
        Optional<ButtonType> saveMenuResult = alert.showAndWait();

        if(event.getSource() == closeAppMenuItem && BookListController.bdc.checkSelection()
                && (BookListController.bdc.checkForChanges() || BookListController.bdc.checkForChangesNewBook())) {
            logger.info("Close App menu Item clicked");

            if (saveMenuResult.get() == yes) {
                logger.info("Yes was pressed");
                BookListController.bdc.fireSave();
                saveMenuResult = stopBookTableGateway();
            } else if (saveMenuResult.get() == no) {
                logger.info("No was pressed");
                saveMenuResult = stopBookTableGateway();
            } else if (saveMenuResult.get() == cancel)
                logger.info("Cancel was pressed");
            else {
                logger.info("No changes on the model were found");
                saveMenuResult = stopBookTableGateway();
            }
        } else if(event.getSource() == showBookListMenuItem && BookListController.bdc.checkSelection()
                && (BookListController.bdc.checkForChanges() || BookListController.bdc.checkForChangesNewBook())) {
            logger.info("Book List menu item clicked.");

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
        } else if(event.getSource() == newBookMenuItem && BookListController.bdc.checkSelection()
                && (BookListController.bdc.checkForChanges() || BookListController.bdc.checkForChangesNewBook())) {
            logger.info("New Book menu item clicked");

            if (BookListController.bdc == null) {
                logger.info("Book Detail Controller is null");
                List<Book> books = BookTableGateway.getInstance().getBooks();
                BookListController.setSelection(books.get(0));
                switchView(ViewType.BOOKDETAILVIEW);
            }

            if (saveMenuResult.get == yes) {
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
    }

    private ButtonType stopBookTableGateway() {
        try {
            btg.stop();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Platform.exit();
        return null;
    }

    private ButtonType switchBookListView() {
        switchView(ViewType.BOOKLISTVIEW);
        BookDetailController.verifySave = false;
        return null;
    }

    private ButtonType addBookView() {
        switchView(ViewType.ADDBOOKVIEW);
        BookDetailController.verifySave = false;
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
        MasterController controller = null;
        switch(viewType){
            case BOOKLISTVIEW:
                view = "BookListView.fxml";
                controller = new BookListController();
                logger.info("Switching to BookListView");
                break;
            case BOOKDETAILVIEW:
                view = "BookDetailView.fxml";
                controller = new BookDetailController(BookTableGateway.read(book.getBookID()));
                logger.info("Switching to BookDetailView");
                break;
            case NEWBOOKVIEW:
                view = "BookDetailView.fxml";
                controller = new BookDetailController(new Book());
                break;
            case SAVEDETAILCHANGES:
                view = "SaveDetailChanges.fxml";
                controller = new SaveDetailChangesController();
                break;
            case AUDITTRAILVIEW:
                view = "AuditTrailView.fxml";
                List<AuditTrailEntry> auditTrail = BookListController.getSelection().getAuditTrail();
                controller = new AuditTrailController(auditTrail);
                break;
        }
        try {
            loadScreen(view, controller);
        } catch (IOException e){
            e.printStackTrace();
            logger.error("IOException");
        }
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

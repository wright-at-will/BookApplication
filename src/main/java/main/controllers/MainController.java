package main.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import main.models.Book;
import main.models.BookDataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;



public class MainController implements Initializable {
	
	public enum ViewType{
		BOOKDETAIL, BOOKLIST
	}
    private static final Logger logger = LogManager.getLogger();

    private static MainController instance = null;
    private BookDataStore bookDataStore;
    private MainController() {
    }

    public static MainController getInstance() {
        if(instance == null)
            instance = new MainController();
        return instance;
    }
    public void setBookDataStore(BookDataStore bookDataStore){
        this.bookDataStore = bookDataStore;
    }


    @FXML
    private BorderPane rootPane;

    // eclipse used the wrong import: import java.awt.event.ActionEvent;
    // void onBeer(ActionEvent event) {
    @FXML
    void onBook() {
        logger.info("Clicked on Book");

        showView(ViewType.BOOKDETAIL);
    }
    
    public void showView(ViewType viewType) {
        // load view according to viewType and plug into center of rootPane
        FXMLLoader loader = null;
        Pane viewNode = new Pane();
        BookController controller = null;
        switch(viewType) {
            case BOOKDETAIL :
                Book book = new Book(bookDataStore);
                loader = new FXMLLoader(getClass().getClassLoader().getResource("BookDetailView.fxml"));
                //this.getClass();
                controller = new BookDetailController(book);// BookDetailController(book);

                loader.setController(controller);
                break;
            case BOOKLIST :
                loader = new FXMLLoader(getClass().getClassLoader().getResource("BookListView.fxml"));
                controller = new BookListController();
                break;
        }

        loader.setController(controller);
        try {
            viewNode = loader.load();
        } catch (Exception e) {
            logger.error("What is happening?");
            e.printStackTrace();
            logger.error("Did not set viewNode properly");
        }
        if(viewNode == null){
            logger.info(viewNode);
        }
        logger.info("Past checking if viewNode is null");
        try {
            rootPane.setCenter(viewNode);
        } catch (Exception e){
            e.printStackTrace();

            logger.error("Did not setCenter properly");
        }
    }
	
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

}

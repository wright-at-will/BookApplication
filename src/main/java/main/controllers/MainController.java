package main.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
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

    private MainController() {
    }

    public static MainController getInstance() {
        if(instance == null)
            instance = new MainController();
        return instance;
    }

    @FXML
    private BorderPane rootPane;

    // eclipse used the wrong import: import java.awt.event.ActionEvent;
    // void onBeer(ActionEvent event) {
    @FXML
    void onBook(ActionEvent event) {
        logger.info("Clicked on Book");

        showView(ViewType.BOOKDETAIL);
    }
    
    public void showView(ViewType viewType) {
        // load view according to viewType and plug into center of rootPane
        FXMLLoader loader = null;
        Parent viewNode;
        MainController controller = null;
        switch(viewType) {
            case BOOKDETAIL :
                loader = new FXMLLoader(this.getClass().getResource("detail1.fxml"));
                controller = new BookDetailController();
                break;
            case BOOKLIST :
                loader = new FXMLLoader(this.getClass().getResource("detail2.fxml"));
                controller = new BookListController();
                break;
        }
        viewNode = null;
        loader.setController(controller);
        try {
            viewNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rootPane.setCenter(viewNode);
    }
	
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

}

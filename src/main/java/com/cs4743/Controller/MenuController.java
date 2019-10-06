package com.cs4743.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.cs4743.View.ViewType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuController implements Initializable {

    // define MenuController instance for the Singleton class
    private static MenuController instance = null;

    // log4j logger definition
    private static Logger logger = LogManager.getLogger(MenuController.class);

    @FXML
    private BorderPane borderPane;

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
    private MenuItem showBookListMenuItem;
    @FXML
    private MenuItem closeAppMenuItem;
    @FXML
    private MenuItem newBookMenuItem;

    @FXML
    void clickMenuItem(ActionEvent event){
        if(event.getSource() == closeAppMenuItem) {
            logger.info("Exit menu item clicked.");
            Platform.exit();
        }else if(event.getSource() == showBookListMenuItem) {
            logger.info("Book List menu item clicked.");
            switchView(ViewType.BOOKLISTVIEW);
        } else if(event.getSource() == newBookMenuItem) {
            logger.info("New Book menu item clicked");
            switchView(ViewType.BOOKDETAILVIEW);
        }
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
                controller = new BookDetailController();
                logger.info("Switching to BookDetailView");
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
        URL url = this.getClass().getClassLoader().getResource("com/cs4743/view/" +view);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        Parent viewNode = loader.load();
        borderPane.setCenter(viewNode);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}

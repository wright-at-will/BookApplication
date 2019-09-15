package com.cs4743;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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

    // clickMenuItem provides control for the different menu options
    @FXML
    void clickMenuItem(ActionEvent event){
        logger.info("Menu item clicked." + event.getSource().toString());
        if(event.getSource() == closeAppMenuItem) {
            logger.info("Exit menu item clicked.");
            Platform.exit();
        }else if(event.getSource() == showBookListMenuItem) {
            logger.info("Book List menu item clicked.");
            switchView(ViewType.BOOKLISTVIEW);
        }
    }


    // switchView allows the other controllers to switch the view
    public void switchView(ViewType viewType){
        String viewString = "";
        MasterController controller = null;
        switch(viewType){
            case BOOKLISTVIEW:
                viewString = "BookListView.fxml";
                controller = new BookListController();
                logger.info("Switching to BookListView");
                if(controller == null){
                    logger.error("Could not switch controllers");
                    Platform.exit();
                }
                break;
            case BOOKDETAILVIEW:
                viewString = "BookDetailView.fxml";
                controller = new BookDetailController();
                break;
        }
        try {
            URL url = this.getClass().getClassLoader().getResource("com/view/"+viewString);
            if(url == null){
                logger.error("Did not find :"+viewString);
                Platform.exit();
            }
            logger.info("Found url: "+url.toString());
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            Parent viewNode = loader.load();
            if(viewNode == null){
                logger.error("Could not load viewNode");
                Platform.exit();
            }
            // plug viewNode into MainView's borderpane center
            logger.info(viewNode.toString());
            //borderPane.setCenter(viewNode);
        } catch (IOException e){
            e.printStackTrace();
            logger.error("IOException");
        }
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

package com.cs4743;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.logging.log4j;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

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
    private MenuItem closeMenuItem;

    // clickMenuItem provides control for the different menu options
    @FXML
    void clickMenuItem(ActionEvent event){
        if(event.getSource() == closeMenuItem)
            Platform.exit();
        else if(event.getSource() == showBookListMenuItem)
            switchView(ViewType.BOOKLISTVIEW);
    }


    // switchView allows the other controllers to switch the view
    public void switchView(ViewType viewType){
        String viewString = "";
        MasterController controller = null;
        switch(viewType){
            case BOOKLISTVIEW:
                viewString = "BookListView.fxml";
                controller = new BookListController();
                break;
            case BOOKDETAILVIEW:
                viewString = "BookDetailView.fxml";
                controller = new BookDetailController();
                break;
        }
        try {
            URL url = this.getClass().getResource(viewString);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            Parent viewNode = loader.load();

            // plug viewNode into MainView's borderpane center
            borderPane.setCenter(viewNode);
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

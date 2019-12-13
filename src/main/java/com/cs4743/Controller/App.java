package com.cs4743.Controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MenuController controller;
    private static Logger log = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) throws IOException {
        controller = MenuController.getInstance();
        scene = new Scene(loadFXML("LandingScreen"));
        stage.setScene(scene);
        stage.setTitle("Book Inventory System");
        stage.show();
        createLogin(stage);
    }

    private void createLogin(Stage parentStage) throws IOException{
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        Scene loginScene = new Scene(new FXMLLoader(App.class.getClassLoader().getResource("com/cs4743/View/LoginView.fxml")).load());
        stage.setScene(loginScene);
        stage.setOnCloseRequest(event ->{
            //Ignore close request
            System.out.println("User tried to close login window");
            exit();//Platform.exit();
        });
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("com/cs4743/View/" + fxml + ".fxml"));
        // sets the primary controller as the menu controller
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
        return;
    }

    public static void exit(){
        Platform.exit();
        System.exit(0);
    }

}
package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Services.BookTableGateway;
import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MenuController controller;

    @Override
    public void start(Stage stage) throws IOException {
        controller = MenuController.getInstance();
        scene = new Scene(loadFXML("LandingScreen"));
        stage.setScene(scene);
        stage.setTitle("Book Inventory System");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("com/cs4743/view/" + fxml + ".fxml"));
        // sets the primary controller as the menu controller
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
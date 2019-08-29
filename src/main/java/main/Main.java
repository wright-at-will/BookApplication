package main;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private static Logger logger = LogManager.getLogger();

    public static void main(String args[]){
        System.out.println("Hello World");

        logger.error("hello error");

        logger.info("hello debug");

        launch(args);

    }


    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
       // FXMLLoader loader = new FXMLLoader();
       // loader.setLocation( Main.class.getResource("view/Main.fxml") );
      //  root = (AnchorPane) loader.load();
        Scene scene = new Scene( root );

        primaryStage.setScene( scene );
        primaryStage.show();
    }
}

package main;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.fxml.*;
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
        Pane pane = new Pane();
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation( Main.class.getResource("Main.fxml") );
        loader.setLocation(getClass().getClassLoader().getResource("FXML/Main.fxml"));
        try{
            pane = (Pane) loader.load();
        } catch (Exception e){
            System.err.println(e.toString() + "\nLoader failed");

        }
        root = new AnchorPane(pane);
        Scene scene = new Scene( root );

        primaryStage.setScene( scene );
        primaryStage.show();
    }
}

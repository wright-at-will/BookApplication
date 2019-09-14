package main;

import javafx.application.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.*;
import main.controllers.BookController;
import main.controllers.BookListController;
import main.controllers.MainController;
import main.models.Book;
import main.models.BookDataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private static Logger logger = LogManager.getLogger();

    public static void main(String args[]){
        System.out.println("Hello World");

        logger.error("hello error");

        logger.info("hello debug");

        launch(args);

    }

    @Override
    public void start(Stage stage) {
        logger.info("in start method");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main_view.fxml"));
        MainController controller = MainController.getInstance();
        loader.setController(controller);

        Parent rootNode = null;
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(rootNode));
        stage.setTitle("Demo");

        stage.show();


    }

    public ArrayList<Book> createBookList(){
        BookDataStore bookDataStore = new BookDataStore();
        ArrayList<Book> bookList = new ArrayList<Book>();
        return bookList;
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        logger.info("Calling stop...");
    }


}

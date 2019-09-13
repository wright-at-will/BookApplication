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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Main.fxml"));
        MainController controller = MainController.getInstance();
        loader.setController(controller);

        Parent rootNode = null;
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        /*Book book = new Book(new BookDataStore());
        loader = new FXMLLoader(this.getClass().getResource("view/BookDetailView.fxml"));
        //this.getClass();
        BookController bookController = new BookListController();// BookDetailController(book);
        loader.setController(bookController);
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        stage.setScene(new Scene(rootNode));
        stage.setTitle("Demo");
        //Test the views
        stage.show();

        // controller.showView(ViewType.DETAIL1);
    }
    /*
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root;
        Pane pane = new Pane();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("main/view/Main.fxml"));
        //loader.setLocation(getClass().getClassLoader().getResource("FXML/Main.fxml"));
        loader.setController(MainController.getInstance());
        try{
            pane = (Pane) loader.load();
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.toString() + "\nLoader failed");
        }
        root = new AnchorPane(pane);
        Scene scene = new Scene( root );

        primaryStage.setScene( scene );
        primaryStage.show();
    }
    */
    public ArrayList<Book> createBookList(){
        BookDataStore bookDataStore = new BookDataStore();
        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book(bookDataStore));
        return bookList;
    }


}

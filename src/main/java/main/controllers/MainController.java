package main.controllers;

import main.constants.ViewType;
import main.models.Book;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;






public class MainController implements Initializable {
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

    @FXML
    void onBook(ActionEvent event) {
        logger.info("Clicked on Book");

        showView(ViewType.BOOKLIST, null);
    }

    public void showView(ViewType viewType, Object data) {
        // load view according to viewType and plug into center of rootPane
        FXMLLoader loader = null;
        Parent viewNode;
        BookController controller = null;
        switch(viewType) {
            case BOOKDETAIL:
                loader = new FXMLLoader(getClass().getResource("/main/view/BookDetailView.fxml"));
                controller = new BookDetailController((Book) data);
                break;
            case BOOKLIST :
                loader = new FXMLLoader(getClass().getResource("/main/view/BookListView.fxml"));
                // make some beers
                //Book book = new Book("Schlitz");
                //List<Book> books = new ArrayList<Book>();
                //books.add(book);
                //book = new Book("Budweiser");
                //books.add(book);
                //controller = new BookListController(books);
                //load in the books

                List<Book> bookList = loadBooks();
                controller = new BookListController(bookList);
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

    private List<Book> loadBooks(){
        List<Book> bookList;
        URL url = getClass().getResource("/main.TextDataStore/books.txt");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        InputStream is = getClass().getResourceAsStream("/main.TextDataStore/books.txt");
        InputStreamReader isr = new InputStreamReader(is);
        try {
            br = new BufferedReader(isr);
            bookList = loadCSV(br);
            //br.close();
        } catch (Exception e) {
            e.printStackTrace();
            if(url != null)
            logger.error("Found but could not open file \n"+url.toString());
            else
                logger.error("Could not find file");
            return null;
        }
    return bookList;
    }

    private List<Book> loadCSV(BufferedReader br) throws Exception{
        List<Book> bookList = new ArrayList<>();
        String line;
        line = br.readLine();
        while((line = br.readLine()) !=null){
            String[] splitLine = "\"*\"".split(line);
            Book book = new Book(splitLine[0]);
            book.setYearPublished(splitLine[1]);
            book.setIsbn(splitLine[2]);
            book.setSummary(splitLine[3]);
            bookList.add(book);
        }
        return bookList;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

}

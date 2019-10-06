package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Model.BookListIndex;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.View.ViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookListController implements Initializable, MasterController {
    private static int bookIndex = 0;
    private static Logger logger = LogManager.getLogger(BookListController.class);

    @FXML
    private Hyperlink book1Hyperlink, book2Hyperlink, book3Hyperlink, book4Hyperlink, book5Hyperlink;

    @FXML
    private GridPane bookListGrid;
    @FXML
    ListView<String> bookList = new ListView<>();

    BookTableGateway btg = new BookTableGateway();

    public static int getBookIndex(){
        return bookIndex;
    }

    // depending on the viewList index, set the bookListIndex ***CURRENTLY HARD CODED***
    public void setBookIndex(int vlIndex){
            if(vlIndex < 0)
                return;
            bookIndex = vlIndex;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ObservableList<Book> books;
        ObservableList<String> books = FXCollections.observableArrayList(btg.bookList());
        // initialize the books in the listView
        bookList.setItems(books);
        // detect mouse double click on each of the cells in the listView
        bookList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                    int viewListIndex = bookList.getSelectionModel().getSelectedIndex();

                    logger.info("The view list was double clicked");
                    if(viewListIndex == -1) {
                        logger.error("Clicked empty area on the list view");
                    } else {
                        logger.info("Clicked book located at position: " + viewListIndex);
                        setBookIndex(viewListIndex+1);
                        MenuController.getInstance().switchView(ViewType.BOOKDETAILVIEW);
                    }
                }

            }

        });

    }

}

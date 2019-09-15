package com.cs4743;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookListController implements Initializable, MasterController {
    private static BookListIndex bookIndex = null;
    private static Logger logger = LogManager.getLogger(BookListController.class);

    @FXML
    private Hyperlink book1Hyperlink, book2Hyperlink, book3Hyperlink, book4Hyperlink, book5Hyperlink;

    @FXML
    ListView<String> bookList = new ListView<String>();

    // populate the list view with the titles of the books. ***CURRENTLY HARD CODED***
    ObservableList<String> books = FXCollections.observableArrayList (
            "The Giving Tree", "Of Mice and Men", "Lord of the Flies", "To Kill a Mockingbird", "The Great Gatsby");

    public static BookListIndex getBookIndex(){
        return bookIndex;
    }

    // depending on the viewList index, set the bookListIndex ***CURRENTLY HARD CODED***
    public void setBookIndex(int vlIndex){
        switch(vlIndex){
            case 0:
                bookIndex = BookListIndex.BOOK1;
                break;
            case 1:
                bookIndex = BookListIndex.BOOK2;
                break;
            case 2:
                bookIndex = BookListIndex.BOOK3;
                break;
            case 3:
                bookIndex = BookListIndex.BOOK4;
                break;
            case 4:
                bookIndex = BookListIndex.BOOK5;
                break;
            default:
                logger.error("Could not assign new book from the given index");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // initialize the books in the listView
        bookList.setItems(books);

        // detect mouse double click on each of the cells in the listView
        bookList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                    int viewListIndex = bookList.getSelectionModel().getSelectedIndex();

                    logger.info("The view list was double clicked");
                    //set the bookListIndex that will be used to generate the book information
                    setBookIndex(viewListIndex);
                    if(viewListIndex >= 0 && viewListIndex <= 4)
                        MenuController.getInstance().switchView(ViewType.BOOKDETAILVIEW);
                    if(viewListIndex == -1)
                        logger.error("Clicked empty area on the list view");
                }

            }

        });

    }

}

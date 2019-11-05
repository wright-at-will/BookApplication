package com.cs4743.Controller;

import com.cs4743.Model.Book;
import com.cs4743.Services.BookTableGateway;
import com.cs4743.View.ViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;

public class BookListController implements Initializable, MasterController {

    private static int bookIndex = 0;
    private static Logger logger = LogManager.getLogger(BookListController.class);
    public static Book emptyBook, selected;
    public static ObservableList<Book> bookListHistory = FXCollections.observableArrayList();
    public static BookDetailController bdc;
    BookTableGateway btg = new BookTableGateway();

    @FXML
    private Hyperlink book1Hyperlink, book2Hyperlink, book3Hyperlink, book4Hyperlink, book5Hyperlink;
    @FXML
    private GridPane bookListGrid;
    @FXML
    ListView<Book> bookList = new ListView<>();
    @FXML
    private Button deleteButton;

    private BookListController(List<Book> books) {
        bookListHistory.clear();
        emptyBook = books.get(0);
        for (int i = 1; i < books.size(); i++) {
            bookListHistory.add(books.get(i));
        }
    }

    @FXML
    private void deleteRecord(){
        Book selectedBook = bookList.getSelectionModel().getSelectedItem();
        logger.info("Selected book: {} bookID: {}", selectedBook.getTitle(), selectedBook.getBookID());
        //btg.delete(selectedBook);
        //bookList.getItems().remove(selectedBook);
    }

    public static BookDetailController makeDetail() {
        if (BookListController.getSelection() == null)
            System.out.println("Selection is null while trying to create detail");
        bdc = new BookDetailController(BookListController.getSelection());
        return bdc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize the books in the listView
        bookList.setEditable(true);
        bookList.setItems(bookListHistory);
        bookList.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book item, boolean isEmpty) {
                super.updateItem(item, isEmpty);
                if (isEmpty || item == null || item.getTitle() == null)
                    setText(null);
                else
                    setText(item.getTitle());
            }
        });

        // detect mouse double click on each of the cells in the listView
        bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                    setSelected(bookList.getSelectionModel().getSelectedItem());
                    int viewListIndex = bookList.getSelectionModel().getSelectedIndex();
                    logger.info("The view list was double clicked");
                    if(viewListIndex == -1) {
                        logger.error("Clicked empty area on the list view");
                    } else {
                        logger.info("Clicked book located at position: " + viewListIndex);
                        MenuController.getInstance().setDetailView(bookList.getSelectionModel().getSelectedItem());
                    }
                }

            }

        });

        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Book selectedBook = bookList.getSelectionModel().getSelectedItem();
                logger.info("Selected book: {} bookID: {}", selectedBook.getTitle(), selectedBook.getBookID());
                btg.delete(selectedBook);
                bookList.getItems().remove(selectedBook);
            }
        });
    }

    //public String toString() { return selected.getTitle(); }

    public static void setSelected(Book book) { selected = book; }

    public static Book getSelection() { return selected; }

    private static MasterController controller = null;

    public static MasterController getBookListController(List<Book> books) { return controller = new BookListController(books); }

    public static MasterController getBookListController() { return controller; }

    public void addToList(Book book) { bookListHistory.add(book); }

    public static ObservableList<Book> getBookList() { return bookListHistory; }

    public static int getBookIndex(){ return bookIndex; }
    // depending on the viewList index, set the bookListIndex ***CURRENTLY HARD CODED***
    public void setBookIndex(int vlIndex){ if(vlIndex < 0) return; bookIndex = vlIndex; }

}

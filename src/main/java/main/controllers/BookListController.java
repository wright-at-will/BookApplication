package main.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.constants.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.models.Book;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController implements Initializable, BookController {
	private static final Logger logger = LogManager.getLogger();

	@FXML
	private ListView<Book> bookList;

	private List<Book> beers;

	public BookListController(List<Book> beers) {
		this.beers = beers;
	}

	@FXML
	void onListClick(MouseEvent event) {
		if(event.getClickCount() == 2) {
			Book selectedBeer = bookList.getSelectionModel().getSelectedItem();
			if(selectedBeer != null) {
				MainController.getInstance().showView(ViewType.BOOKDETAIL, selectedBeer);
				logger.info("HELP: " + selectedBeer);
			}
		}
	}

	@FXML
	void doButton(ActionEvent event) {
		// logger.error("switch to view 1");
		// MainController.getInstance().showView(ViewType.DETAIL1);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Book> tempBeers = FXCollections.observableArrayList(beers);
		bookList.setItems(tempBeers);

	}

}

/*
package main.controllers;

import main.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListController implements BookController {
	
	private ArrayList<Book> bookList = new ArrayList<Book>();
	public BookListController(List<Book> books){

	}
}
*/
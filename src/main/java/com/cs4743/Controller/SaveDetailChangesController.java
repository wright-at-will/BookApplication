package com.cs4743.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SaveDetailChangesController implements Initializable, MasterController {

    public static String choice = null;

    @FXML
    private Button yesButton, noButton, cancelButton;

    @FXML
    void clickedNoButton(ActionEvent event) { choice = "no"; }

    @FXML
    void clickedYesButton(ActionEvent event) {
        choice = "yes";
    }

    @FXML
    void clickedCancelButton(ActionEvent event) { choice = "cancel"; }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

    }

}
package com.cs4743.Controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private static Logger log = LogManager.getLogger(LoginController.class);
    public LoginController(){
        userFilled = false;
        passFilled = false;


    }


    private boolean userFilled,passFilled;

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    @FXML
    Button cancelButton;
    @FXML
    Button loginButton;
    @FXML
    private void clickLogin() throws NoSuchAlgorithmException{
      String secretHash = getHash(passwordField.getText());
      String username = usernameField.getText();
      log.info("username: "+username +"\npasswordHash: "+secretHash);
      //Now to try to log in
    }
    @FXML
    private void clickCancel(){
        //Handle closing
        App.exit();
    }




    private static String getHash(String password) throws NoSuchAlgorithmException {
        String encodedPassword;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        encodedPassword = bytesToHex(digest.digest(password.getBytes()));
        return encodedPassword;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @FXML
    public void initialize(){
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(passwordField.textProperty(),
                        usernameField.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (passwordField.getText().isEmpty()
                        || usernameField.getText().isEmpty());
            }

        };

        loginButton.disableProperty().bind(bb);

    }
}

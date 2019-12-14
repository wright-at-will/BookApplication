package com.cs4743.Controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private static Logger log = LogManager.getLogger(LoginController.class);
    public LoginController(){
        userFilled = false;
        passFilled = false;
        mc = MenuController.getInstance();

    }

    private MenuController mc;
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
        try {
            if (sendRequest(usernameField.getText(), passwordField.getText())) {
                //Handle working credentials
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @FXML
    private void clickCancel(){
        //Handle closing
        App.exit();
    }

    public boolean sendRequest(String username,String password) throws URISyntaxException, NoSuchAlgorithmException, ClientProtocolException, IOException {

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http")
                .setHost("localhost")
                .setPort(8888)
                .setPath("/login")
                .addParameter("username", username)
                .addParameter("password", getHash(password));
        URI uri = uriBuilder.build();
        log.info("Built uri");

        HttpGet getMethod = new HttpGet(uri);
        HttpClient client =
                HttpClientBuilder
                        .create()
                        .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                        .setDefaultCookieStore(mc.getCookieStore())
                        .build();

        log.info("Built client");
        HttpResponse response = client.execute(getMethod);
        log.info("Got response");
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            log.info("Response failed");
            return false;
        }
        log.info("Response passed");
        return true;
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

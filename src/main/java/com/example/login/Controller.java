package com.example.login;
// log-in.fxml

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_signUp;
    @FXML
    private TextField tf_username;
    @FXML
    private Label captcha;
    @FXML
    private TextField tf_captcha;
    @FXML
    private PasswordField pf_password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tf_username.requestFocus();
        captcha.setText(new RandomString().randomAlphaNumeric(5));

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tf_captcha.getText().equals(captcha.getText())) {
                    DBUtils.logInUser(event, tf_username.getText(), pf_password.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Captcha incorrect!!!");
                    alert.show();
                    captcha.setText(new RandomString().randomAlphaNumeric(5));
                }
            }
        });

        btn_signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "Sign Up!", null, null);
            }
        });

    }
}
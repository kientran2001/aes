package com.example.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button btn_signUp;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirmPassword;
    @FXML
    private Label captcha;
    @FXML
    private TextField tf_captcha;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tf_username.requestFocus();
        captcha.setText(new RandomString().randomAlphaNumeric(5));

        btn_signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().trim().isEmpty() && !pf_password.getText().trim().isEmpty()) {
                    // DBUtils.signUpUser(event, tf_username.getText(), pf_password.getText(), pf_confirmPassword.getText());
                    if(tf_captcha.getText().equals(captcha.getText())){
                        DBUtils.signUpUser(event, tf_username.getText(), pf_password.getText(), pf_confirmPassword.getText());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Captcha incorrect!!!");
                        alert.show();
                        captcha.setText(new RandomString().randomAlphaNumeric(5));
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "log-in.fxml", "Log in!", null, null);
            }
        });

    }
}

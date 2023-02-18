package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ShowPassword {
    @FXML
    private TextField masterPass;
    @FXML
    private Button confirmBtn;
    @FXML
    private TextField tfPass;

    private String masterPassword;
    private String decryptPass;

    private int count;

    public void setInformation(String password, String decryptPass, int count) {
        masterPassword = password;
        this.decryptPass = decryptPass;
        this.count = count;
    }

    public void confirm() {
        String mPass = masterPass.getText().trim();
        if(mPass.equals(masterPassword)) {
            tfPass.setText(decryptPass);
            this.count++;
        }
    }

    public int getCount() {
        return this.count;
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                labelPass.setText(decryptPass);
//            }
//        };
//
//        Timer timer = new Timer(Thread.currentThread().getName());
//        long delay = 10000L;
//        timer.schedule(task, delay);
//    }
}

package com.example.login;

import com.example.login.xmlfile.ModifyXML;
import com.example.login.xmlfile.ReadListXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static com.example.login.KeyStore.StoreToKeyStore;

public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String password){
        Parent root = null;

        if(username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LogOutController logOutController = loader.getController();
                logOutController.setUserInformation(username, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static User checkUserExists(String username, List<User> listUsers) {
        for (User user : listUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public static void signUpUser(ActionEvent event, String username, String password, String confirmPassword) {
        try {
            List<User> listUsers = ReadListXML.readListUsers();

            if (checkUserExists(username, listUsers) != null) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists! You can't use this username.");
                alert.show();
            } else {
                if (!password.equals(confirmPassword)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Password and confirm password must be equal.");
                    alert.show();
                } else {
                    SecureRandom random = new SecureRandom();
                    byte[] salt = new byte[16];
                    random.nextBytes(salt);
                    String hmac = Base64.getEncoder().encodeToString(HMAC.calculateHMAC(password, String.valueOf(salt)).getBytes());

                    ModifyXML.addUser(username, salt.toString(), hmac);

                    String seed = new RandomString().randomAlphaNumeric(16);
                    SecretKey keyApp = new SecretKeySpec(seed.getBytes(), "AES");
                    String ksFile = "ks" + username + ".keystore";
                    try {
                        StoreToKeyStore(keyApp, password, ksFile);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    changeScene(event, "log-out.fxml", "Welcome " + username + " !!!", username, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void logInUser(ActionEvent event, String username, String password) {

        try {

            List<User> listUsers = ReadListXML.readListUsers();

            if (checkUserExists(username, listUsers) == null) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                User user = checkUserExists(username, listUsers);
                // Reformat String from XML file
                String salt = user.getSalt();
//                System.out.println(salt);
                String hmac = user.getHmac().trim();
//                System.out.println(hmac);
                String retrievedHmac = Base64.getEncoder().encodeToString(HMAC.calculateHMAC(password, salt).getBytes());
//                System.out.println(retrievedHmac);

                if (retrievedHmac.equals(hmac)) {
                    changeScene(event, "log-out.fxml", "Welcome " + username + " !!!", username, password);
                } else {
                    System.out.println("Password did not match!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Provided credentials are incorrect!");
                    alert.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

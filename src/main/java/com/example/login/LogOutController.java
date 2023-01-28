package com.example.login;

import com.example.login.xmlfile.ModifyXML;
import com.example.login.xmlfile.ReadListXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import static com.example.login.KeyStore.LoadFromKeyStore;
import static com.example.login.KeyStore.StoreToKeyStore;

public class LogOutController implements Initializable {

    @FXML
    private Button btn_logOut, btn_add, btn_search, btn_delete, btn_generate, btn_show;
    @FXML
    private Label label_userAccount, addPass;
    @FXML
    private TextField addAcc, addWeb, searchAcc;
    @FXML
    private TableView<Account> tableInfo;
    @FXML
    private TableColumn<Account, String> accountCol;
    @FXML
    private TableColumn<Account, String> passwordCol;
    @FXML
    private TableColumn<Account, String> webCol;
    private ObservableList<Account> accountList;

//    SecretKey keyLoad = LoadFromKeyStore("test.keystore", "123456");

    private SecretKey keyLoad;
    private SecretKey key;
    private SecretKeySpec Ks;
//    String str = keyLoad.toString();
//    String seed = str.substring(str.length() - 16);
    private String str, seed;
    private String uName, mPass;

    List<Account> listAccounts = ReadListXML.readListAccounts();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "log-in.fxml", "Log in!", null, null);
            }
        });

        btn_generate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                String randNumber = RandomString.randomAlphaNumeric(16);
                byte[] randNumber = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(randNumber);
                // Base64, get 12 charactors
                String encodedString = Base64.getEncoder().encodeToString(randNumber);
                addPass.setText(encodedString.substring(0, 12));
            }
        });
    }

    public void setUserInformation(String username, String password) {
        label_userAccount.setText("Username: " + username + " Password: " + password);
        keyLoad = LoadFromKeyStore("ks" + username + ".keystore", password);
        str = keyLoad.toString();
        seed = str.substring(str.length() - 16);
        System.out.println("seed: " + seed);

        uName = username;
        mPass = password;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), seed.getBytes(), 65536, 128);
//            KeySpec spec = new PBEKeySpec(password.toCharArray(), seed.getBytes(), 65536, 128);
            System.out.println("password: " + password);
            System.out.println("seed: " + seed);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            key = factory.generateSecret(spec);
            System.out.println("key: " + key);
            Ks = new SecretKeySpec(key.getEncoded(), "AES");
            System.out.println("Ks: " + Ks);

            List<Account> listAccounts = ReadListXML.readListAccounts();
            accountList = FXCollections.observableArrayList(listAccounts).filtered(account -> account.getuName().equalsIgnoreCase(uName));
            accountCol.setCellValueFactory(new PropertyValueFactory<Account, String>("acc"));
            passwordCol.setCellValueFactory(new PropertyValueFactory<Account, String>("pass"));
            webCol.setCellValueFactory(new PropertyValueFactory<Account, String>("web"));
            tableInfo.setItems(accountList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent actionEvent) {
        try {
            // encrypt with PKCS7 padding
//            Security.addProvider(new BouncyCastleProvider());
//            Cipher encrypterWithPad = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
//            AlgorithmParameterSpec IVspec = new IvParameterSpec(seed.getBytes());
//            encrypterWithPad.init(Cipher.ENCRYPT_MODE, Ks, IVspec);
//            byte[] encryptedPass = encrypterWithPad.doFinal(addPass.getText().getBytes());
            byte[] encryptedPass = encrypt(addPass.getText().getBytes(), key, seed.getBytes());
            System.out.println("encryptPass: " + encryptedPass);

            String acc = addAcc.getText().trim();
            String pass = Base64.getEncoder().encodeToString(encryptedPass);
            String web = addWeb.getText().trim();

            if(acc.equals("") || pass.equals("") || web.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all information!");
                alert.show();
            } else {
                if (checkAccountExists(acc, listAccounts, uName) != null) {
                    System.out.println("Account already exists!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Account already exists!");
                    alert.show();
                } else {
                    ModifyXML.addAccount(uName, acc, pass, web);
//                listAccounts.add(new Account(uName, acc, addPass.getText(), web));
                    List<Account> listAccounts = ReadListXML.readListAccounts();
                    accountList = FXCollections.observableArrayList(listAccounts).filtered(account -> account.getuName().equalsIgnoreCase(uName));
                    tableInfo.setItems(accountList);
                    addAcc.clear();
                    addPass.setText("");
                    addWeb.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int count = 0;
    public void show(ActionEvent actionEvent) throws Exception {
        try {
            Account selected = tableInfo.getSelectionModel().getSelectedItem();
            String pass = selected.getPass();
            System.out.println("pass: " + pass);
            byte[] encryptPass = Base64.getDecoder().decode(pass);
            System.out.println("EncryptPass: " + encryptPass);
            String decryptPass = decrypt(encryptPass, key, seed.getBytes());
            System.out.println("DecryptedPass: " + decryptPass);

            if(count % 3 == 0) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("show-password.fxml"));
                Parent root = loader.load();
                ShowPassword controller = loader.getController();
                controller.setInformation(mPass, decryptPass, count);

                Stage stage1 = new Stage();
                stage1.setTitle("Show password!");
                stage1.initModality(Modality.APPLICATION_MODAL);
                stage1.setScene(new Scene(root, 450, 250));
                stage1.showAndWait();
                count = controller.getCount();
                System.out.println("count: " + count);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Show password!");
                alert.setHeaderText("Are you want to show this password?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == null) {
                    System.out.println("No selection!");
                } else if (option.get() == ButtonType.OK) {
                    Alert alertPass = new Alert(Alert.AlertType.INFORMATION);
                    alertPass.setTitle("Password");
                    alertPass.setHeaderText("Your password!!!");
                    alertPass.setContentText(decryptPass);
                    alertPass.show();
                    count++;
                    System.out.println("count: " + count);
                } else if (option.get() == ButtonType.CANCEL) {
                    System.out.println("Cancelled!");
                } else {
                    System.out.println("-");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent) throws Exception {
        Account selected = tableInfo.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account!");
        alert.setHeaderText("Are you want to delete this account?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            System.out.println("No selection!");
        } else if (option.get() == ButtonType.OK) {
            ModifyXML.deleteAccount(selected);
            List<Account> listAccounts = ReadListXML.readListAccounts();
            accountList = FXCollections.observableArrayList(listAccounts).filtered(account -> account.getuName().equalsIgnoreCase(uName));
            tableInfo.setItems(accountList);
        } else if (option.get() == ButtonType.CANCEL) {
            System.out.println("Cancelled!");
        } else {
            System.out.println("-");
        }
    }

    public void search(ActionEvent actionEvent) throws Exception {
        String s = searchAcc.getText().trim();
        List<Account> listAccounts = ReadListXML.readListAccounts();
        if (s.equals("")) {
            accountList = FXCollections.observableArrayList(listAccounts).filtered(account -> account.getuName().equalsIgnoreCase(uName));
        } else {
            List<Account> result = listAccounts.stream().filter(account -> account.getWeb().contains(s)).collect(Collectors.toList());
            accountList = FXCollections.observableArrayList(result).filtered(account -> account.getuName().equalsIgnoreCase(uName));
        }
        tableInfo.setItems(accountList);
    }

    public Account checkAccountExists(String acc, List<Account> listAccounts, String username) {
        for (Account account : listAccounts) {
            if (account.getuName().equals(username) && account.getAcc().equals(acc)) {
                return account;
            }
        }
        return null;
    }

    public String decryptPass(String pass) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        AlgorithmParameterSpec IVspec = new IvParameterSpec(seed.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, Ks, IVspec);
        byte[] decryptedText = cipher.doFinal(pass.getBytes());
        return new String(decryptedText);
    }

    public static byte[] encrypt (byte[] plaintext,SecretKey key,byte[] IV ) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}

module com.example.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.xml.bind;
    requires org.bouncycastle.provider;


    opens com.example.login to javafx.fxml;
    exports com.example.login;
    exports com.example.login.xmlfile;
    opens com.example.login.xmlfile to javafx.fxml;
}
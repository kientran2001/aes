package com.example.login.xmlfile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class CreateXML {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.newDocument();
//
//            // tạo phần tử gốc có tên User-Detail
//            Element rootUser = doc.createElement("User-Detail");
//            // thêm thuộc tính totalUsers vào thẻ class
//            doc.appendChild(rootUser);
//            Attr totalUserAttr = doc.createAttribute("totalUsers");
//            rootUser.setAttributeNode(totalUserAttr);
//
//            // tạo phần tử User
//            Element User = doc.createElement("User");
//            rootUser.appendChild(User);
//            // tạo userId, username, salt, hmac cho user
////            Element userId = doc.createElement("userId");
////            User.appendChild(userId);
//            Element username = doc.createElement("username");
//            User.appendChild(username);
//            Element salt = doc.createElement("salt");
//            User.appendChild(salt);
//            Element hmac = doc.createElement("hmac");
//            User.appendChild(hmac);
//
//            // ghi nội dung vào file XML
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File("users.xml"));
//            transformer.transform(source, result);


            DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
            Document doc1 = dBuilder1.newDocument();

            // tạo phần tử gốc có tên Account-Detail
            Element rootAccount = doc1.createElement("Account-Detail");
            // thêm thuộc tính totalUsers vào thẻ class
            doc1.appendChild(rootAccount);

            // tạo phần tử Account
            Element Account = doc1.createElement("Account");
            rootAccount.appendChild(Account);

//            Element id = doc1.createElement("id");
//            Account.appendChild(id);
            Element uName = doc1.createElement("uName");
            Account.appendChild(uName);
            Element acc = doc1.createElement("acc");
            Account.appendChild(acc);
            Element pass = doc1.createElement("pass");
            Account.appendChild(pass);
            Element web = doc1.createElement("web");
            Account.appendChild(web);

            // ghi nội dung vào file XML
            TransformerFactory transformerFactory1 = TransformerFactory.newInstance();
            Transformer transformer1 = transformerFactory1.newTransformer();
            DOMSource source1 = new DOMSource(doc1);
            StreamResult result1 = new StreamResult(new File("accounts.xml"));
            transformer1.transform(source1, result1);

            // ghi kết quả ra console để kiểm tra
//            StreamResult consoleResult = new StreamResult(System.out);
//            transformer.transform(source, consoleResult);
//            transformer.transform(source1, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
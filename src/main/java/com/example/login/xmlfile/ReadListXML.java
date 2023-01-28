package com.example.login.xmlfile;

import com.example.login.Account;
import com.example.login.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadListXML {
    public static List<User> readListUsers() {
        List<User> listUsers = new ArrayList<>();
        User user = null;

        try {
            // đọc file input.xml
            File inputFile = new File("users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // in phần tử gốc ra màn hình
//            System.out.println("Phần tử gốc:"
//                    + doc.getDocumentElement().getNodeName());

            // đọc tất cả các phần tử có tên thẻ là "user"
            NodeList nodeListUser = doc.getElementsByTagName("User");

            // duyệt các phần tử user
            for (int i = 0; i < nodeListUser.getLength(); i++) {
                // tạo đối tượng user
                user = new User();
                // đọc các thuộc tính của user
                Node nNode = nodeListUser.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    user.setUsername(eElement.getElementsByTagName("username")
                            .item(0).getTextContent());
                    user.setSalt(eElement.getElementsByTagName("salt")
                            .item(0).getTextContent());
                    user.setHmac(eElement.getElementsByTagName("hmac")
                            .item(0).getTextContent());
                }
                // add đối tượng user vào listUsers
                listUsers.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUsers;
    }

    public static List<Account> readListAccounts() {
        List<Account> listAccounts = new ArrayList<>();
        Account account = null;

        try {
            // đọc file input.xml
            File inputFile = new File("accounts.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // đọc tất cả các phần tử có tên thẻ là "Account"
            NodeList nodeListAccount = doc.getElementsByTagName("Account");

            // duyệt các phần tử user
            for (int i = 0; i < nodeListAccount.getLength(); i++) {
                // tạo đối tượng user
                account = new Account();
                // đọc các thuộc tính của user
                Node nNode = nodeListAccount.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    account.setuName(eElement.getElementsByTagName("uName").item(0).getTextContent());
                    account.setAcc(eElement.getElementsByTagName("acc").item(0).getTextContent());
                    account.setPass(eElement.getElementsByTagName("pass").item(0).getTextContent());
                    account.setWeb(eElement.getElementsByTagName("web").item(0).getTextContent());

                }
                // add đối tượng user vào listUsers
                listAccounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAccounts;
    }

//    public static List<Account> listAccountUName(List<Account> listAccount, String username) {
//        for (Account account : listAccount) {
//            if(account.getuName() != username) {
//                listAccount.remove(account);
//            }
//        }
//        return listAccount;
//    }
}

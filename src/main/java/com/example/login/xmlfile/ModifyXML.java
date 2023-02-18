package com.example.login.xmlfile;

import com.example.login.Account;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ModifyXML {
    public static void addUser(String username, String salt, String hmac) {
        try {
            File inputFile = new File("users.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);

            Element rootUser = doc.getDocumentElement();
            Element User = doc.createElement("User");
            rootUser.appendChild(User);

            Element addUsername = doc.createElement("username");
            addUsername.appendChild(doc.createTextNode(username));
            User.appendChild(addUsername);

            Element addPass = doc.createElement("salt");
            addPass.appendChild(doc.createTextNode(salt));
            User.appendChild(addPass);

            Element addWeb = doc.createElement("hmac");
            addWeb.appendChild(doc.createTextNode(hmac));
            User.appendChild(addWeb);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("users.xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(String uName, String acc, String pass, String web) {
        try {
            File inputFile = new File("accounts.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);

            Element rootAccount = doc.getDocumentElement();
            Element Account = doc.createElement("Account");
            rootAccount.appendChild(Account);

//            Element addId = doc.createElement("id");
//            addId.appendChild(doc.createTextNode(id));
//            Account.appendChild(addId);

            Element adduName = doc.createElement("uName");
            adduName.appendChild(doc.createTextNode(uName));
            Account.appendChild(adduName);

            Element addAcc = doc.createElement("acc");
            addAcc.appendChild(doc.createTextNode(acc));
            Account.appendChild(addAcc);

            Element addPass = doc.createElement("pass");
            addPass.appendChild(doc.createTextNode(pass));
            Account.appendChild(addPass);

            Element addWeb = doc.createElement("web");
            addWeb.appendChild(doc.createTextNode(web));
            Account.appendChild(addWeb);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("accounts.xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccount(Account account) {
        try {
            File inputFile = new File("accounts.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);

            Element rootAccount = doc.getDocumentElement();
            NodeList nodeListAccount = doc.getElementsByTagName("Account");

            for (int i = 0; i <nodeListAccount.getLength(); i++) {

                Node node = nodeListAccount.item(i);
                NodeList childList = node.getChildNodes();

                boolean isSelecteduName = false;
                boolean isSelectedAcc = false;
                boolean isSelectedWeb = false;

                // Looking through all children nodes
                for (int x = 0; x < childList.getLength(); x++) {
                    Node child = childList.item(x);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if(child.getNodeName().equalsIgnoreCase("uName")
                                && child.getTextContent().equalsIgnoreCase(account.getuName())){
                            isSelecteduName = true;
                        }
                    }
                }

                for (int x = 0; x < childList.getLength(); x++) {
                    Node child = childList.item(x);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if(child.getNodeName().equalsIgnoreCase("acc")
                                && child.getTextContent().equalsIgnoreCase(account.getAcc())){
                            isSelectedAcc = true;
                        }
                    }
                }

                for (int x = 0; x < childList.getLength(); x++) {
                    Node child = childList.item(x);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if(child.getNodeName().equalsIgnoreCase("web")
                                && child.getTextContent().equalsIgnoreCase(account.getWeb())){
                            isSelectedWeb = true;
                        }
                    }
                }

                boolean isSelected = isSelecteduName && isSelectedAcc && isSelectedWeb;

                if(isSelected) {
                    rootAccount.removeChild(node);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("accounts.xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

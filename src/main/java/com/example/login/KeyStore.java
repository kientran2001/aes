package com.example.login;

import javax.crypto.SecretKey;
import java.io.*;

public class KeyStore {
//    public static void main(String[] args) {
//        String seed = new RandomString().randomAlphaNumeric(16);
//        SecretKey keyApp = new SecretKeySpec(seed.getBytes(), "AES");
//        System.out.println(seed);
//        System.out.println(keyApp);
//
//        try {
//            StoreToKeyStore(keyApp, "123456", "test.keystore");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    public static void StoreToKeyStore(SecretKey keyToStore, String password, String filepath) throws Exception {
        File file = new File(filepath);
        java.security.KeyStore javaKeyStore = java.security.KeyStore.getInstance("JCEKS");
        if(!file.exists()) {
            javaKeyStore.load(null, null);
        }

        javaKeyStore.setKeyEntry("keyAlias", keyToStore, password.toCharArray(), null);
        OutputStream writeStream = new FileOutputStream(filepath);
        javaKeyStore.store(writeStream, password.toCharArray());
    }

    public static SecretKey LoadFromKeyStore(String filepath, String password) {
        try {
            java.security.KeyStore keyStore = java.security.KeyStore.getInstance("JCEKS");
            InputStream readStream = new FileInputStream(filepath);
            keyStore.load(readStream, password.toCharArray());
            SecretKey key = (SecretKey) keyStore.getKey("keyAlias", password.toCharArray());
            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.example.login;

public class Account {
    public Account(String uName, String acc, String pass, String web) {
        this.uName = uName;
        this.acc = acc;
        this.pass = pass;
        this.web = web;
    }

    public Account() {
    }

    private String uName;
    private String acc;
    private String pass;
    private String web;

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}

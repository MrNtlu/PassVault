package com.mrntlu.PassVault.Offline.Models;

import io.realm.RealmObject;

public class MailObject extends RealmObject {

    private String mail;
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

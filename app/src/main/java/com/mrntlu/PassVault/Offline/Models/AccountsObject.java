package com.mrntlu.PassVault.Offline.Models;

import io.realm.RealmObject;

public class AccountsObject extends RealmObject {

    private String idMail;
    private String password;
    private String description;

    public String getIdMail() {
        return idMail;
    }

    public void setIdMail(String idMail) {
        this.idMail = idMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

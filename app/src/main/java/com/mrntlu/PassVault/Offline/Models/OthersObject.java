package com.mrntlu.PassVault.Offline.Models;

import io.realm.RealmObject;

public class OthersObject extends RealmObject {

    private String description;
    private String password;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

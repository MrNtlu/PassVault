package com.mrntlu.PassVault.Offline.Models;

import io.realm.kotlin.types.RealmObject;

class MailObject(): RealmObject {
    var mail: String = ""
    var password: String = ""

    constructor(mail: String, password: String) : this() {
        this.mail = mail
        this.password = password
    }
}


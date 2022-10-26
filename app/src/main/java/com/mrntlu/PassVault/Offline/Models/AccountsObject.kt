package com.mrntlu.PassVault.Offline.Models;

import io.realm.kotlin.types.RealmObject;

class AccountsObject(): RealmObject {
    var idMail: String = ""
    var password: String = ""
    var description: String = ""

    constructor(idMail: String, password: String, description: String) : this() {
        this.idMail = idMail
        this.password = password
        this.description = description
    }
}

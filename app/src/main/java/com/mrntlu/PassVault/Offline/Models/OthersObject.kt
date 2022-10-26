package com.mrntlu.PassVault.Offline.Models;

import io.realm.kotlin.types.RealmObject;

class OthersObject(): RealmObject {
    var password: String = ""
    var description: String = ""

    constructor(password: String, description: String) : this() {
        this.password = password
        this.description = description
    }
}

package com.mrntlu.PassVault.models

import io.realm.kotlin.types.RealmObject

class OfflinePassword(): RealmObject {
    var idMail: String = ""
    var password: String = ""
    var description: String = ""

    constructor(idMail: String, password: String, description: String) : this() {
        this.idMail = idMail
        this.password = password
        this.description = description
    }
}
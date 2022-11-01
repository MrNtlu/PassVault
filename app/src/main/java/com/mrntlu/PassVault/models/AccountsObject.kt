package com.mrntlu.PassVault.models

import io.realm.kotlin.types.RealmObject

class AccountsObject : RealmObject {
    var idMail: String = ""
    var password: String = ""
    var description: String = ""
}

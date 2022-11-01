package com.mrntlu.PassVault.models

import io.realm.kotlin.types.RealmObject

class MailObject : RealmObject {
    var mail: String = ""
    var password: String = ""
}


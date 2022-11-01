package com.mrntlu.PassVault.models

import io.realm.kotlin.types.RealmObject

class OthersObject : RealmObject {
    var password: String = ""
    var description: String = ""
}

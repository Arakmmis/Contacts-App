package me.arakmmis.contactsapp.businesslogic.models

import io.realm.RealmObject
import java.io.Serializable

open class PhoneNumber(
        var number: String,

        var type: String) : Serializable, RealmObject() {

    constructor() : this("", "")
}
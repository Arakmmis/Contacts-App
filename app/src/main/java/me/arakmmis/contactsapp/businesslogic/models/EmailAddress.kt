package me.arakmmis.contactsapp.businesslogic.models

import io.realm.RealmObject
import java.io.Serializable

open class EmailAddress(
        var emailAddress: String,

        var type: String) : Serializable, RealmObject() {

    constructor() : this("", "")
}
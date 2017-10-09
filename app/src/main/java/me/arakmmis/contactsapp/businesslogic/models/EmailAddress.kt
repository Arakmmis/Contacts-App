package me.arakmmis.contactsapp.businesslogic.models

import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

data class EmailAddress(
        @ColumnInfo(name = "email_address")
        val emailAddress: String,

        @ColumnInfo(name = "email_type")
        val type: String) : Serializable
package me.arakmmis.contactsapp.businesslogic.models

import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

data class PhoneNumber(
        @ColumnInfo(name = "number")
        val number: String,

        @ColumnInfo(name = "number_type")
        val type: String) : Serializable
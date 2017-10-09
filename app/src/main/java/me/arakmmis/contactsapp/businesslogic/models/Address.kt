package me.arakmmis.contactsapp.businesslogic.models

import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

data class Address(
        @ColumnInfo(name = "address")
        val address: String,

        @ColumnInfo(name = "address_type")
        val type: String) : Serializable
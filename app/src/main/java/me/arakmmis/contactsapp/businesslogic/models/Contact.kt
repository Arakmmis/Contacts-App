package me.arakmmis.contactsapp.businesslogic.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Contact")
data class Contact(
        @PrimaryKey(autoGenerate = true)
        val id: String,

        @ColumnInfo(name = "profile_pic")
        val profilePic: String,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "date_of_birth")
        val dateOfBirth: String,

        @Embedded
        val phoneNumbers: List<PhoneNumber>,

        @ColumnInfo(name = "default_phone_number")
        val defaultPhoneNumber: String,

        @Embedded
        val addresses: List<Address> = Arrays.asList(),

        @Embedded
        val emailAddresses: List<EmailAddress>) : Serializable
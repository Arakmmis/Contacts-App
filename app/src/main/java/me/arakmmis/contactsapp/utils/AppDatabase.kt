package me.arakmmis.contactsapp.utils

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.arakmmis.contactsapp.businesslogic.contacts.ContactDao
import me.arakmmis.contactsapp.businesslogic.models.Contact

@Database(entities = arrayOf(Contact::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
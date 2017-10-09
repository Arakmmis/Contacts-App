package me.arakmmis.contactsapp.utils

import android.app.Application
import android.arch.persistence.room.Room
import com.orhanobut.hawk.Hawk


object App : Application() {

    fun getDatabase(): AppDatabase = db

    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "my_contacts").build()
    }

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()
        Cache.removePhoneNumbers()
        Cache.removeAddress()
        Cache.removeEmails()
    }
}
package me.arakmmis.contactsapp.utils

import android.app.Application

import com.orhanobut.hawk.Hawk
import io.realm.Realm

class App : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        Realm.init(this);

        realm = Realm.getDefaultInstance()
    }

    companion object {
        var instance: App? = null

        var realm: Realm? = null
    }
}

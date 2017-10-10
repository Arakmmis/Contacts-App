package me.arakmmis.contactsapp.utils

import io.realm.RealmList
import io.realm.RealmModel

class ListUtil<T : RealmModel?> {

    fun listToRealmList(list: List<T>): RealmList<T> {
        val realmList: RealmList<T> = RealmList()

        list.forEach { t: T ->
            realmList.add(t)
        }

        return realmList
    }

    fun realmListToList(realmList: RealmList<T>): List<T> {
        val list: ArrayList<T> = ArrayList()

        realmList.forEach { t: T ->
            list.add(t)
        }

        return list
    }
}
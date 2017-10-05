package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single

/**
 * Created by arakm on 10/4/2017.
 */
interface ContactsManager {

    fun getContacts(): Single<List<Contact>>
}
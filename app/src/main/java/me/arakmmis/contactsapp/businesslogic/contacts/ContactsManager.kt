package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import me.arakmmis.contactsapp.businesslogic.models.Contact

/**
 * Created by arakm on 10/4/2017.
 */
interface ContactsManager {

    fun getContacts(): Single<List<Contact>>
}
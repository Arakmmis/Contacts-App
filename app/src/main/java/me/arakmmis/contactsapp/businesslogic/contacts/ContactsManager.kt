package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import me.arakmmis.contactsapp.businesslogic.models.Contact

interface ContactsManager {

    fun getContacts(): Single<List<Contact>>

    fun insertContact(contact: Contact): Single<Contact>
}
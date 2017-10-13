package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import me.arakmmis.contactsapp.businesslogic.models.Contact

interface ContactsManager {

    fun getContacts(): Single<List<Contact>>

    fun insertContact(contact: Contact): Single<Contact>

    fun getContact(contactId: Int): Single<Contact>

    fun deleteContact(contactId: Int): Single<String>

    fun updateContact(contact: Contact): Single<Contact>

    fun lookForContacts(query: String): Single<List<Contact>>
}
package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.utils.App

class ContactsRepo : ContactsManager {

    override fun getContacts(): Single<List<Contact>> = App.getDatabase().contactDao().getAll()

    override fun insertContact(contact: Contact) = App.getDatabase().contactDao().insert(contact)
}
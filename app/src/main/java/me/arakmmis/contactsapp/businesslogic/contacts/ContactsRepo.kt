package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import io.reactivex.SingleEmitter
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.utils.App
import java.util.*

class ContactsRepo : ContactsManager {

    override fun getContacts(): Single<List<Contact>> = Single.create { received: SingleEmitter<List<Contact>> ->
        App.realm?.executeTransaction { realm ->
            val realmContacts = realm.where(Contact::class.java)
                    .findAll()

            val contacts: ArrayList<Contact> = ArrayList<Contact>()
            realmContacts.forEach { contact -> contacts.add(contact) }

            received.onSuccess(contacts)
        }
    }

    override fun insertContact(contact: Contact): Single<Contact> = Single.create { received: SingleEmitter<Contact> ->
        App.realm?.executeTransaction { realm ->
            realm.insert(contact)
        }

        received.onSuccess(contact)
    }
}
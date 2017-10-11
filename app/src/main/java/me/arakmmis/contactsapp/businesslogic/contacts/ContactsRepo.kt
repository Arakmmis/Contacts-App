package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.realm.Realm
import io.realm.Sort
import me.arakmmis.contactsapp.businesslogic.models.Contact
import java.util.*

class ContactsRepo : ContactsManager {

    override fun getContacts(): Single<List<Contact>> = Single.create { received: SingleEmitter<List<Contact>> ->
        getRealmInstance().executeTransaction { realm ->
            val realmContacts = realm.where(Contact::class.java)
                    .findAll()
                    .sort("name", Sort.ASCENDING)

            val copiedFromRealm = realm.copyFromRealm(realmContacts)
            val contacts: ArrayList<Contact> = ArrayList<Contact>()
            copiedFromRealm.forEach { contact -> contacts.add(contact) }

            received.onSuccess(contacts)
        }
    }

    override fun insertContact(contact: Contact): Single<Contact> = Single.create { received: SingleEmitter<Contact> ->
        getRealmInstance().executeTransaction { realm ->
            realm.insert(contact)
        }

        received.onSuccess(contact)
    }

    override fun getContact(contactId: Int): Single<Contact> = Single.create { received: SingleEmitter<Contact> ->
        getRealmInstance().executeTransaction { realm ->
            val realmContact = realm.where(Contact::class.java)
                    .equalTo("id", contactId)
                    .findFirst()

            received.onSuccess(realm.copyFromRealm(realmContact))
        }
    }

    private fun getRealmInstance(): Realm = Realm.getDefaultInstance()
}
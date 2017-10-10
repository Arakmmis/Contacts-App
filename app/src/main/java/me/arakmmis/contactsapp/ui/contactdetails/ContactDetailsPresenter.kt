package me.arakmmis.contactsapp.ui.contactdetails

import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.TestContactsRepo
import me.arakmmis.contactsapp.mvpcontracts.ContactDetailsContract

class ContactDetailsPresenter(val contactDetailsView: ContactDetailsContract.ContactDetailsView) : ContactDetailsContract.ContactDetailsPresenter {

    val contactsManager: ContactsManager = TestContactsRepo()

    init {
        getContactData()
    }

    private fun getContactData() {
        
    }
}
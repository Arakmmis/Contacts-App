package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Contact

interface ContactDetailsContract {

    interface ContactDetailsView {
        fun setContactData(contact: Contact)

        fun navigateToHome()

        fun toast(message: String)
    }

    interface ContactDetailsPresenter {
        fun deleteContact(contactId: Int)
    }
}
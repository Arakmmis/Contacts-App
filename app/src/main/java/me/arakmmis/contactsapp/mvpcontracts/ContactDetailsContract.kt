package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Contact

interface ContactDetailsContract {

    interface ContactDetailsView {
        fun setContactData(contact: Contact)
    }

    interface ContactDetailsPresenter {

    }
}
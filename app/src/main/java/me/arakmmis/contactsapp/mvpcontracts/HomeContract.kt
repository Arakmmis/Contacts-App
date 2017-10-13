package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Contact

interface HomeContract {

    interface HomeView {
        fun setContacts(contacts: List<Contact>)

        fun toast(message: String)

        fun setSearchResult(contacts: List<Contact>)
    }

    interface HomePresenter {
        fun lookFor(query: String)
    }
}
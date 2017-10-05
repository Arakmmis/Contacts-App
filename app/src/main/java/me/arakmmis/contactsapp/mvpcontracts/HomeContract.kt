package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.contacts.Contact

/**
 * Created by arakm on 10/4/2017.
 */
interface HomeContract {

    interface HomeView {
        fun setContacts(contacts: List<Contact>)

    }

    interface HomePresenter {

    }
}
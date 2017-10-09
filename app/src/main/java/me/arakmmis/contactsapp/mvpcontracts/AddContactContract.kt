package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber

/**
 * Created by arakm on 10/5/2017.
 */
interface AddContactContract {

    interface AddContactView {
        fun showPhoneNumberError(errorMessage: String)

        fun updatePhoneList(phoneNumbers: List<PhoneNumber>)
    }

    interface AddContactPresenter {
        fun addPhoneNumber(phoneNumber: PhoneNumber)

        fun deletePhoneNumber(phoneNumber: PhoneNumber)
    }
}
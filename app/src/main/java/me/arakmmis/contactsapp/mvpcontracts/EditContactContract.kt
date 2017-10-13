package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber

interface EditContactContract {

    interface EditContactView {
        fun setContactData(contact: Contact)

        fun updatePhoneList(phoneNumbers: List<PhoneNumber>)

        fun updateAddressList(addresses: List<Address>)

        fun updateEmailAddressesList(emails: List<EmailAddress>)

        fun navigateToContactDetails(contact: Contact)

        fun showPhoneNumberError(errorMessage: String)

        fun showAddressError(errorMessage: String)

        fun showEmailAddressError(errorMessage: String)

        fun showNameError(errorMessage: String)

        fun showDateError(errorMessage: String)

        fun showPhoneNumbersListError(errorMessage: String)

        fun showEmailsListError(errorMessage: String)

        fun disableFieldError(field: String)
    }

    interface EditContactPresenter {
        fun addPhoneNumber(phoneNumber: PhoneNumber)

        fun deletePhoneNumber(phoneNumber: PhoneNumber)

        fun addAddress(address: Address)

        fun deleteAddress(address: Address)

        fun addEmailAddress(email: EmailAddress)

        fun deleteEmailAddress(email: EmailAddress)

        fun updateContact(contactId: Int, profilePic: ByteArray, name: String, date: String)

        fun editPhoneNumber(newPhoneNumber: PhoneNumber, oldPhoneNumber: PhoneNumber, position: Int)
    }
}
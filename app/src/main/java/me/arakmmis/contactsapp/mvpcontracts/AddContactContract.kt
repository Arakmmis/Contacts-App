package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber

/**
 * Created by arakm on 10/5/2017.
 */
interface AddContactContract {

    interface AddContactView {
        fun showPhoneNumberError(errorMessage: String)

        fun updatePhoneList(phoneNumbers: List<PhoneNumber>)

        fun showAddressError(errorMessage: String)

        fun updateAddressList(addresses: List<Address>)

        fun showEmailAddressError(errorMessage: String)

        fun updateEmailAddressesList(emails: List<EmailAddress>)

        fun navigateToContactDetails(contact: Contact)

        fun showNameError(errorMessage: String)

        fun showDateError(errorMessage: String)

        fun showPhoneNumbersListError(errorMessage: String)

        fun showEmailsListError(errorMessage: String)

        fun disableFieldError(field: String)
    }

    interface AddContactPresenter {
        fun addPhoneNumber(phoneNumber: PhoneNumber)

        fun deletePhoneNumber(phoneNumber: PhoneNumber)

        fun addAddress(address: Address)

        fun deleteAddress(address: Address)

        fun addEmailAddress(email: EmailAddress)

        fun deleteEmailAddress(email: EmailAddress)

        fun addContact(profilePic: ByteArray, contactName: String, contactBirthDate: String)
    }
}
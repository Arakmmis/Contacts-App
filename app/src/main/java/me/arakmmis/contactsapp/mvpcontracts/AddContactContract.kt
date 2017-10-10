package me.arakmmis.contactsapp.mvpcontracts

import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import java.io.File

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
    }

    interface AddContactPresenter {
        fun addPhoneNumber(phoneNumber: PhoneNumber)

        fun deletePhoneNumber(phoneNumber: PhoneNumber)

        fun addAddress(address: Address)

        fun deleteAddress(address: Address)

        fun addEmailAddress(email: EmailAddress)

        fun deleteEmailAddress(email: EmailAddress)

        fun addContact(profilePicFile: File?, contactName: String, contactBirthDate: String)
    }
}
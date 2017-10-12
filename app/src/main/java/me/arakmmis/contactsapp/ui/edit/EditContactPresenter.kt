package me.arakmmis.contactsapp.ui.edit

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.mvpcontracts.EditContactContract
import me.arakmmis.contactsapp.utils.Cache
import me.arakmmis.contactsapp.utils.ValidationUtil

class EditContactPresenter(val view: EditContactContract.EditContactView, val contactId: Int) : EditContactContract.EditContactPresenter {

    val contactsManager: ContactsManager = ContactsRepo()

    init {
        getContactData(contactId)
    }

    private fun getContactData(contactId: Int) {
        contactsManager.getContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Contact> {
                    override fun onSuccess(t: Contact) {
                        view.setContactData(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ECP: getContact", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }

    override fun addPhoneNumber(phoneNumber: PhoneNumber) {
        val result = ValidationUtil.errorsInPhoneNumber(phoneNumber.number)

        if (result == ValidationUtil.NO_ERRORS) {
            var exists = false
            Cache.getPhoneNumbers().forEach { existingPhoneNumber ->
                if (phoneNumber.number == existingPhoneNumber.number)
                    exists = true
            }

            if (!exists) {
                if (phoneNumber.type == "Default") {
                    Cache.setDefaultTypeUsed(true)
                }

                val newNumbers = Cache.getPhoneNumbers()

                newNumbers.add(phoneNumber)
                Cache.setPhoneNumbers(newNumbers)
                view.updatePhoneList(newNumbers)
            } else {
                view.showPhoneNumberError("Phone Number Already Exists")
            }
        } else {
            view.showPhoneNumberError(result)
        }
    }

    override fun deletePhoneNumber(phoneNumber: PhoneNumber) {
        if (phoneNumber.type == "Default") {
            Cache.setDefaultTypeUsed(false)
        }

        val itemToBeDeleted = Cache.getPhoneNumbers().indexOf(phoneNumber)

        val newNumbers: List<PhoneNumber>

        if (Cache.getPhoneNumbers().size == 1) {
            newNumbers = ArrayList<PhoneNumber>()
        } else {
            val numbersBeforeDeleted = Cache.getPhoneNumbers().slice(IntRange(0, itemToBeDeleted - 1))
            val numbersAfterDeleted = Cache.getPhoneNumbers().slice(IntRange(itemToBeDeleted + 1, Cache.getPhoneNumbers().size - 1))
            newNumbers = numbersBeforeDeleted + numbersAfterDeleted
        }

        Cache.setPhoneNumbers(newNumbers)
        view.updatePhoneList(newNumbers)
    }

    override fun addAddress(address: Address) {
        val result = ValidationUtil.errorsInAddress(address.address)

        if (result == ValidationUtil.NO_ERRORS) {
            var exists = false
            Cache.getAddresses().forEach { existingAddress ->
                if (address.address == existingAddress.address)
                    exists = true
            }

            if (!exists) {
                val newAddress = Cache.getAddresses()

                newAddress.add(address)
                Cache.setAddresses(newAddress)
                view.updateAddressList(newAddress)
            } else {
                view.showAddressError("Address Already Exists")
            }
        } else {
            view.showAddressError(result)
        }
    }

    override fun deleteAddress(address: Address) {
        val itemToBeDeleted = Cache.getAddresses().indexOf(address)

        val newAddresses: List<Address>

        if (Cache.getAddresses().size == 1) {
            newAddresses = ArrayList<Address>()
        } else {
            val addressesBeforeDeleted = Cache.getAddresses().slice(IntRange(0, itemToBeDeleted - 1))
            val addressesAfterDeleted = Cache.getAddresses().slice(IntRange(itemToBeDeleted + 1, Cache.getAddresses().size - 1))
            newAddresses = addressesBeforeDeleted + addressesAfterDeleted
        }

        Cache.setAddresses(newAddresses)
        view.updateAddressList(newAddresses)
    }

    override fun addEmailAddress(email: EmailAddress) {
        val result = ValidationUtil.errorsInEmail(email.emailAddress)

        if (result == ValidationUtil.NO_ERRORS) {
            var exists = false
            Cache.getEmails().forEach { existingEmail ->
                if (email.emailAddress == existingEmail.emailAddress)
                    exists = true
            }

            if (!exists) {
                val newEmails = Cache.getEmails()

                newEmails.add(email)
                Cache.setEmails(newEmails)
                view.updateEmailAddressesList(newEmails)
            } else {
                view.showEmailAddressError("Email Address Already Exists")
            }
        } else {
            view.showEmailAddressError(result)
        }
    }

    override fun deleteEmailAddress(email: EmailAddress) {
        val itemToBeDeleted = Cache.getEmails().indexOf(email)

        val newEmails: List<EmailAddress>

        if (Cache.getEmails().size == 1) {
            newEmails = ArrayList<EmailAddress>()
        } else {
            val emailsBeforeDeleted = Cache.getEmails().slice(IntRange(0, itemToBeDeleted - 1))
            val emailsAfterDeleted = Cache.getEmails().slice(IntRange(itemToBeDeleted + 1, Cache.getEmails().size - 1))
            newEmails = emailsBeforeDeleted + emailsAfterDeleted
        }

        Cache.setEmails(newEmails)
        view.updateEmailAddressesList(newEmails)
    }

    private fun validateInput(name: String, birthDate: String): Boolean {
        val results = ValidationUtil.validateAddContactInput(name, birthDate, Cache.getPhoneNumbers(), Cache.getEmails())

        var proceed = true

        if (results[ValidationUtil.NAME_KEY] != ValidationUtil.NO_ERRORS) {
            view.showNameError(errorMessage = results[ValidationUtil.NAME_KEY] as String)
            proceed = false
        } else
            view.disableFieldError(field = ValidationUtil.NAME_KEY)

        if (results[ValidationUtil.DATE_KEY] != ValidationUtil.NO_ERRORS) {
            view.showDateError(errorMessage = results[ValidationUtil.DATE_KEY] as String)
            proceed = false
        } else
            view.disableFieldError(field = ValidationUtil.DATE_KEY)

        if (results[ValidationUtil.PHONE_NUMBERS_KEY] != ValidationUtil.NO_ERRORS) {
            view.showPhoneNumbersListError(errorMessage = results[ValidationUtil.PHONE_NUMBERS_KEY] as String)
        } else
            view.disableFieldError(field = ValidationUtil.PHONE_NUMBERS_KEY)

        if (results[ValidationUtil.EMAILS_KEY] != ValidationUtil.NO_ERRORS) {
            view.showEmailsListError(errorMessage = results[ValidationUtil.EMAILS_KEY] as String)
        } else
            view.disableFieldError(field = ValidationUtil.EMAILS_KEY)

        return proceed
    }
}
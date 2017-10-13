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
import me.arakmmis.contactsapp.utils.ListUtil
import me.arakmmis.contactsapp.utils.ValidationUtil

class EditContactPresenter(val view: EditContactContract.EditContactView, val contactId: Int) : EditContactContract.EditContactPresenter {

    private val contactsManager: ContactsManager = ContactsRepo()

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

        if (Cache.getPhoneNumbers().size == 1) {
            Cache.setPhoneNumbers(ArrayList<PhoneNumber>())
            view.updatePhoneList(Cache.getPhoneNumbers())
        } else {
            val newNumbers = ArrayList<PhoneNumber>()
            Cache.getPhoneNumbers().forEach { existingPhoneNumber ->
                if (phoneNumber.number != existingPhoneNumber.number)
                    newNumbers.add(existingPhoneNumber)
            }
            Cache.setPhoneNumbers(newNumbers)
            view.updatePhoneList(newNumbers)
        }
    }

    override fun editPhoneNumber(newPhoneNumber: PhoneNumber, oldPhoneNumber: PhoneNumber, position: Int) {
        val result = ValidationUtil.errorsInPhoneNumber(newPhoneNumber.number)

        if (result == ValidationUtil.NO_ERRORS) {
            if (newPhoneNumber.number == oldPhoneNumber.number &&
                    newPhoneNumber.type == oldPhoneNumber.type) {

                view.updatePhoneList(Cache.getPhoneNumbers())

            } else if (newPhoneNumber.number == oldPhoneNumber.number &&
                    newPhoneNumber.type != oldPhoneNumber.type) {

                if (newPhoneNumber.type == "Default") {
                    Cache.setDefaultTypeUsed(true)
                } else if (oldPhoneNumber.type == "Default" && newPhoneNumber.type != "Default") {
                    Cache.setDefaultTypeUsed(false)
                }

                val newNumbers = Cache.getPhoneNumbers()

                newNumbers.set(position, newPhoneNumber)
                Cache.setPhoneNumbers(newNumbers)
                view.updatePhoneList(newNumbers)
            } else {

                var exists = false

                Cache.getPhoneNumbers().forEach { existingPhoneNumber ->
                    if (newPhoneNumber.number == existingPhoneNumber.number)
                        exists = true
                }

                if (!exists) {
                    if (newPhoneNumber.type == "Default") {
                        Cache.setDefaultTypeUsed(true)
                    } else if (oldPhoneNumber.type == "Default" && newPhoneNumber.type != "Default") {
                        Cache.setDefaultTypeUsed(false)
                    }

                    val newNumbers = Cache.getPhoneNumbers()

                    newNumbers.set(position, newPhoneNumber)
                    Cache.setPhoneNumbers(newNumbers)
                    view.updatePhoneList(newNumbers)
                } else {
                    view.showPhoneNumberError("Phone Number Already Exists")
                }
            }
        } else {
            view.showPhoneNumberError(result)
        }
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
        val newAddresses = ArrayList<Address>()

        if (Cache.getAddresses().size != 1) {
            Cache.getAddresses().forEach { existingAddress ->
                if (address.address != existingAddress.address)
                    newAddresses.add(existingAddress)
            }
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
        val newEmails = ArrayList<EmailAddress>()

        if (Cache.getEmails().size != 1) {
            Cache.getEmails().forEach { existingEmail ->
                if (email.emailAddress != existingEmail.emailAddress)
                    newEmails.add(existingEmail)
            }
        }

        Cache.setEmails(newEmails)
        view.updateEmailAddressesList(newEmails)
    }

    override fun updateContact(contactId: Int, profilePic: ByteArray, name: String, date: String) {
        if (validateInput(name, date)) {
            val contact = Contact(id = contactId,
                    name = name,
                    profilePic = profilePic,
                    dateOfBirth = date,
                    phoneNumbers = ListUtil<PhoneNumber>().listToRealmList(Cache.getPhoneNumbers()),
                    defaultPhoneNumber = if (Cache.getPhoneNumbers().filter { phoneNumber -> phoneNumber.type == "Default" }.isEmpty()) Cache.getPhoneNumbers()[0].number
                    else Cache.getPhoneNumbers().filter { phoneNumber -> phoneNumber.type == "Default" }[0].number,
                    addresses = ListUtil<Address>().listToRealmList(Cache.getAddresses()),
                    emailAddresses = ListUtil<EmailAddress>().listToRealmList(Cache.getEmails()))

            contactsManager.updateContact(contact)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Contact> {
                        override fun onError(e: Throwable) {
                            Log.e("ECP: updateContact", e.message)
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onSuccess(contact: Contact) {
                            Log.d("ECP: updateContact", "Contact: " + contact.name)
                            view.navigateToContactDetails(contact)
                        }
                    })
        }
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
            proceed = false
        } else
            view.disableFieldError(field = ValidationUtil.PHONE_NUMBERS_KEY)

        if (results[ValidationUtil.EMAILS_KEY] != ValidationUtil.NO_ERRORS) {
            view.showEmailsListError(errorMessage = results[ValidationUtil.EMAILS_KEY] as String)
            proceed = false
        } else
            view.disableFieldError(field = ValidationUtil.EMAILS_KEY)

        return proceed
    }
}
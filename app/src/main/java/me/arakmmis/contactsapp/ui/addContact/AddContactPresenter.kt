package me.arakmmis.contactsapp.ui.addContact

import android.graphics.BitmapFactory
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.TestContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract
import me.arakmmis.contactsapp.utils.*
import java.io.File

class AddContactPresenter(private val addContactView: AddContactContract.AddContactView) : AddContactContract.AddContactPresenter {

    private val contactsManager: ContactsManager = TestContactsRepo()

    override fun addPhoneNumber(phoneNumber: PhoneNumber) {
        val result = ValidationUtil.errorsInPhoneNumber(phoneNumber.number)

        if (result == ValidationUtil.NO_ERRORS) {
            if (!Cache.getPhoneNumbers().contains(phoneNumber)) {

                if (phoneNumber.type == "Default") {
                    Cache.setDefaultTypeUsed(true)
                }

                val newNumbers = Cache.getPhoneNumbers()

                newNumbers.add(phoneNumber)
                Cache.setPhoneNumbers(newNumbers)
                addContactView.updatePhoneList(newNumbers)
            } else {
                addContactView.showPhoneNumberError("Phone Number already entered")
            }
        } else {
            addContactView.showPhoneNumberError(result)
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
        addContactView.updatePhoneList(newNumbers)
    }

    override fun addAddress(address: Address) {
        val result = ValidationUtil.errorsInAddress(address.address)

        if (result == ValidationUtil.NO_ERRORS) {
            if (!Cache.getAddresses().contains(address)) {

                val newAddress = Cache.getAddresses()

                newAddress.add(address)
                Cache.setAddresses(newAddress)
                addContactView.updateAddressList(newAddress)
            } else {
                addContactView.showAddressError("Address already entered")
            }
        } else {
            addContactView.showAddressError(result)
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
        addContactView.updateAddressList(newAddresses)
    }

    override fun addEmailAddress(email: EmailAddress) {
        val result = ValidationUtil.errorsInEmail(email.emailAddress)

        if (result == ValidationUtil.NO_ERRORS) {
            if (!Cache.getEmails().contains(email)) {

                val newEmails = Cache.getEmails()

                newEmails.add(email)
                Cache.setEmails(newEmails)
                addContactView.updateEmailAddressesList(newEmails)
            } else {
                addContactView.showEmailAddressError("Email Address already entered")
            }
        } else {
            addContactView.showEmailAddressError(result)
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
        addContactView.updateEmailAddressesList(newEmails)
    }

    override fun addContact(profilePicFile: File?, contactName: String, contactBirthDate: String) {
        Log.d("ACP: addContact", "Default Number: " + Cache.getPhoneNumbers().filter { phoneNumber -> phoneNumber.type == "Default" }[0].number)

        val contact = Contact(name = contactName,
                profilePic = ByteArrayUtil.fromFile(profilePicFile) ?:
                        ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                dateOfBirth = contactBirthDate,
                phoneNumbers = ListUtil<PhoneNumber>().listToRealmList(Cache.getPhoneNumbers()),
                defaultPhoneNumber = Cache.getPhoneNumbers().filter { phoneNumber -> phoneNumber.type == "Default" }[0].number,
                addresses = ListUtil<Address>().listToRealmList(Cache.getAddresses()),
                emailAddresses = ListUtil<EmailAddress>().listToRealmList(Cache.getEmails()))

        contactsManager.insertContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Contact> {
                    override fun onError(e: Throwable) {
                        Log.e("ACP: addContact", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(contact: Contact) {
                        Log.d("ACP: addContact", "Contact: " + contact.name)
                        addContactView.navigateToContactDetails(contact)
                    }
                })
    }
}
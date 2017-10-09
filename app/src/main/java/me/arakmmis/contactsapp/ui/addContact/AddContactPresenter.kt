package me.arakmmis.contactsapp.ui.addContact

import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract
import me.arakmmis.contactsapp.utils.Cache
import me.arakmmis.contactsapp.utils.ValidationUtil

class AddContactPresenter(private val addContactView: AddContactContract.AddContactView) : AddContactContract.AddContactPresenter {

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
}
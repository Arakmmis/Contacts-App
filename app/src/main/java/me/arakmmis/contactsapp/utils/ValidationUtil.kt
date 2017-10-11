package me.arakmmis.contactsapp.utils

import android.text.TextUtils
import android.util.Patterns
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber

object ValidationUtil {

    val NAME_KEY = "name"
    val DATE_KEY = "date"
    val PHONE_NUMBERS_KEY = "phone_numbers"
    val EMAILS_KEY = "emails_key"

    val NO_ERRORS = "No Errors found"

    fun errorsInPhoneNumber(phoneNumber: String): String {

        if (isFieldEmpty(phoneNumber))
            return generateEmptyErrorMessage("Phone Number")
        else if (!isPhoneNumberLengthValid(phoneNumber))
            return "Phone Number can't be less than 7 numbers"

        return NO_ERRORS
    }

    fun errorsInEmail(email: String): String {

        if (isFieldEmpty(email))
            return generateEmptyErrorMessage("Email")
        else if (!isEmailFormatValid(email))
            return generateNotValidFormatErrorMessage("Email")

        return NO_ERRORS
    }

    fun errorsInAddress(address: String): String {

        if (isFieldEmpty(address))
            return generateEmptyErrorMessage("Address")

        return NO_ERRORS
    }

    private fun errorsInDate(date: String): String {

        if (isFieldEmpty(date))
            return generateEmptyErrorMessage("Date")
        else if (date == App.instance?.resources?.getString(R.string.click_to_pick_date_of_birth))
            return generateNotValidFormatErrorMessage("Date")

        return NO_ERRORS
    }

    fun validateAddContactInput(name: String, date: String, phoneNumbers: List<PhoneNumber>, emailAddresses: List<EmailAddress>): Map<String, String> {
        val input = HashMap<String, String>()

        input.put(NAME_KEY, if (isFieldEmpty(name)) generateEmptyErrorMessage("Name") else NO_ERRORS)
        input.put(DATE_KEY, errorsInDate(date))
        input.put(PHONE_NUMBERS_KEY, if (phoneNumbers.isEmpty()) generateEmptyListErrorMessage("Phone Numbers") else NO_ERRORS)
        input.put(EMAILS_KEY, if (emailAddresses.isEmpty()) generateEmptyListErrorMessage("Email Address") else NO_ERRORS)

        return input
    }

    private fun isFieldEmpty(field: String): Boolean = TextUtils.isEmpty(field)

    private fun isPhoneNumberLengthValid(password: String): Boolean = password.length >= 7

    private fun isEmailFormatValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).find()

    private fun generateEmptyErrorMessage(field: String): String = "$field can't be empty"

    private fun generateEmptyListErrorMessage(field: String): String = "$field needs at least one entry"

    private fun generateNotValidFormatErrorMessage(field: String): String = "$field format isn't valid"
}
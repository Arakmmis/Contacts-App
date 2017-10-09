package me.arakmmis.contactsapp.utils

import android.text.TextUtils
import android.util.Patterns

object ValidationUtil {

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

    private fun isFieldEmpty(field: String): Boolean = TextUtils.isEmpty(field)

    private fun isPhoneNumberLengthValid(password: String): Boolean = password.length >= 7

    private fun isEmailFormatValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).find()

    private fun generateEmptyErrorMessage(field: String): String = "$field can't be empty"

    private fun generateNotValidFormatErrorMessage(field: String): String = "$field format isn't valid"
}
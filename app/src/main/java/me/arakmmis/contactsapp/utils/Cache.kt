package me.arakmmis.contactsapp.utils

import com.orhanobut.hawk.Hawk
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import java.util.*

object Cache {

    fun setPhoneNumbers(phoneNumbers: List<PhoneNumber>) = Hawk.put(Const.PHONE_NUMBERS_KEY, phoneNumbers)

    fun getPhoneNumbers(): ArrayList<PhoneNumber> {
        if (!Hawk.contains(Const.PHONE_NUMBERS_KEY)) {
            return ArrayList<PhoneNumber>()
        }

        return Hawk.get(Const.PHONE_NUMBERS_KEY)
    }

    fun removePhoneNumbers() = Hawk.delete(Const.PHONE_NUMBERS_KEY)

    fun setDefaultTypeUsed(answer: Boolean) = Hawk.put(Const.DEFAULT_TYPE_KEY, answer)

    fun isDefaultTypeUsed(): Boolean {
        if (!Hawk.contains(Const.DEFAULT_TYPE_KEY)) {
            return false
        }

        return Hawk.get(Const.DEFAULT_TYPE_KEY)
    }
}
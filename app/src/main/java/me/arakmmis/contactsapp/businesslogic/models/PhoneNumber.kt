package me.arakmmis.contactsapp.businesslogic.models

import java.io.Serializable

/**
 * Created by arakm on 10/5/2017.
 */
data class PhoneNumber(val number: String,
                       val type: String) : Serializable
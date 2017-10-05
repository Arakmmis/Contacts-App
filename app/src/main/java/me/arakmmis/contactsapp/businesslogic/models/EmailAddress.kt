package me.arakmmis.contactsapp.businesslogic.models

import java.io.Serializable

/**
 * Created by arakm on 10/5/2017.
 */
data class EmailAddress(val emailAddress: String,
                        val type: String) : Serializable
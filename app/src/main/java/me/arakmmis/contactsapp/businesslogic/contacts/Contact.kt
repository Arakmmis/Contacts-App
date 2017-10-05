package me.arakmmis.contactsapp.businesslogic.contacts

import java.io.Serializable
import java.util.*

/**
 * Created by arakm on 10/4/2017.
 */
data class Contact(val id: String,
                   val profilePic: String,
                   val name: String,
                   val phoneNumbers: List<String>,
                   val defaultPhoneNumber: String,
                   val addresses: List<String> = Arrays.asList(),
                   val emailAddresses: List<String>) : Serializable
package me.arakmmis.contactsapp.businesslogic.models

import java.io.Serializable
import java.util.*

/**
 * Created by arakm on 10/4/2017.
 */
data class Contact(val id: String,
                   val profilePic: String,
                   val name: String,
                   val dateOfBirth: String,
                   val phoneNumbers: List<PhoneNumber>,
                   val defaultPhoneNumber: String,
                   val addresses: List<Address> = Arrays.asList(),
                   val emailAddresses: List<EmailAddress>) : Serializable
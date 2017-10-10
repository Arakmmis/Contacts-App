package me.arakmmis.contactsapp.businesslogic.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

open class Contact(
        @PrimaryKey
        var id: Int = ThreadLocalRandom.current().nextInt(0, 100000 + 1),

        var profilePic: ByteArray,

        var name: String,

        var dateOfBirth: String,

        var phoneNumbers: RealmList<PhoneNumber>,

        var defaultPhoneNumber: String,

        var addresses: RealmList<Address> = RealmList(),

        var emailAddresses: RealmList<EmailAddress>) : Serializable, RealmObject() {

    constructor() : this(0, ByteArray(0), "", "", RealmList<PhoneNumber>(), "", RealmList<Address>(), RealmList<EmailAddress>())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (id != other.id) return false
        if (!Arrays.equals(profilePic, other.profilePic)) return false
        if (name != other.name) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (phoneNumbers != other.phoneNumbers) return false
        if (defaultPhoneNumber != other.defaultPhoneNumber) return false
        if (addresses != other.addresses) return false
        if (emailAddresses != other.emailAddresses) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + Arrays.hashCode(profilePic)
        result = 31 * result + name.hashCode()
        result = 31 * result + dateOfBirth.hashCode()
        result = 31 * result + phoneNumbers.hashCode()
        result = 31 * result + defaultPhoneNumber.hashCode()
        result = 31 * result + addresses.hashCode()
        result = 31 * result + emailAddresses.hashCode()
        return result
    }
}
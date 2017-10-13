package me.arakmmis.contactsapp.businesslogic.contacts

import android.graphics.BitmapFactory
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.realm.RealmList
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.utils.App
import me.arakmmis.contactsapp.utils.ByteArrayUtil
import java.util.*

class TestContactsRepo : ContactsManager {

    override fun getContacts(): Single<List<Contact>> {
        return Single.create { received: SingleEmitter<List<Contact>> ->
            received.onSuccess(Arrays.asList(
                    Contact(
                            profilePic = ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                            name = "Abdel Rahman Abdel Kader",
                            dateOfBirth = "20 Feb 1996",
                            phoneNumbers = RealmList<PhoneNumber>(PhoneNumber("+201127400311", "Work"), PhoneNumber("01127400311", "Other"),
                                    PhoneNumber("55525000", "School")),
                            defaultPhoneNumber = "01127400311",
                            addresses = RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            emailAddresses = RealmList<EmailAddress>(EmailAddress("arakmmis@gmail.com", "Work"), EmailAddress("arakmmis@outlook.com", "Personal"),
                                    EmailAddress("devil-play@hotmail.com", "School"))
                    ),
                    Contact(
                            profilePic = ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                            name = "Mohamed Ahmed",
                            dateOfBirth = "1 Jan 1995",
                            phoneNumbers = RealmList<PhoneNumber>(PhoneNumber("01127498172", "Other"), PhoneNumber("+20191827111", "Home"),
                                    PhoneNumber("98021789042", "Home")),
                            defaultPhoneNumber = "98021789042",
                            addresses = RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str., Alexandria, Egypt", "Home")),
                            emailAddresses = RealmList<EmailAddress>(EmailAddress("worube@p33.org", "Work"), EmailAddress("wabinizo@p33.org", "Other"))
                    ),
                    Contact(
                            profilePic = ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                            name = "Abdel Rahman El Ghamry",
                            dateOfBirth = "22 Aug 1994",
                            phoneNumbers = RealmList<PhoneNumber>(PhoneNumber("+223896400311", "Work"), PhoneNumber("0192180123", "Work"),
                                    PhoneNumber("92361729836", "Work")),
                            defaultPhoneNumber = "0192180123",
                            addresses = RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy", "Work"), Address("1190 Pine Garden Lane", "Home")),
                            emailAddresses = RealmList<EmailAddress>(EmailAddress("sadekirepe@p33.org", "Personal"))
                    ),
                    Contact(
                            4,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Islam El Desoky",
                            "1 Aug 1995",
                            RealmList<PhoneNumber>(PhoneNumber("+98162874962", "Work"), PhoneNumber("0507791448", "Home")),
                            "0507791448",
                            RealmList<Address>(Address("6 Omar Bin El 5atab", "Home"), Address("2768 Peck Street", "Other"),
                                    Address("2090 Briarwood Drive", "Work")),
                            RealmList<EmailAddress>(EmailAddress("tacoxubimu@p33.org", "Work"), EmailAddress("jukara@p33.org", "School"),
                                    EmailAddress("niwupeta@p33.org", "Personal"), EmailAddress("yave@p33.org", "Personal"),
                                    EmailAddress("kiluci@p33.org", "Other"))
                    ),
                    Contact(
                            5,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Doha Mohammed Ammar",
                            "5 May 1995",
                            RealmList<PhoneNumber>(PhoneNumber("01119868731", "Home")),
                            "01119868731",
                            RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Work"),
                                    Address("4203 Flanigan Oaks Drive", "School"), Address("106 Lauren Drive", "Home"), Address("4975 Woodbridge Lane", "Home")),
                            RealmList<EmailAddress>(EmailAddress("cocarimodi@p33.org", "Personal"))
                    ),
                    Contact(
                            6,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Sohaila Mohamed Ahmed Al Alem",
                            "17 Dec 1995",
                            RealmList<PhoneNumber>(PhoneNumber("+1-518-555-0144", "Mobile"), PhoneNumber("518-555-0144", "Other"),
                                    PhoneNumber("518-555-0111", "Mobile"), PhoneNumber("+1-202-555-0163", "Work"), PhoneNumber("202-555-0163", "Work")),
                            "518-555-0144",
                            RealmList<Address>(),
                            RealmList<EmailAddress>(EmailAddress("vometiroye@p33.org", "Personal"), EmailAddress("donejaneko@p33.org", "Work"))
                    ),
                    Contact(
                            7,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Alaa Mostafa",
                            "1 Jan 1995",
                            RealmList<PhoneNumber>(PhoneNumber("+1-518-555-0144", "Home")),
                            "+1-518-555-0144",
                            RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            RealmList<EmailAddress>(EmailAddress("tufakiz@p33.org", "Personal"))
                    )
            ))
        }
    }

    override fun insertContact(contact: Contact): Single<Contact> {
        return Single.create { received: SingleEmitter<Contact> ->
            received.onSuccess(contact)
        }
    }

    override fun getContact(contactId: Int): Single<Contact> {
        return Single.create { received: SingleEmitter<Contact> ->
            received.onSuccess(Contact(
                    4,
                    ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Islam El Desoky",
                    "1 Aug 1995",
                    RealmList<PhoneNumber>(PhoneNumber("+98162874962", "Work"), PhoneNumber("0507791448", "Home")),
                    "0507791448",
                    RealmList<Address>(Address("6 Omar Bin El 5atab", "Home"), Address("2768 Peck Street", "Other"),
                            Address("2090 Briarwood Drive", "Work")),
                    RealmList<EmailAddress>(EmailAddress("tacoxubimu@p33.org", "Work"), EmailAddress("jukara@p33.org", "School"),
                            EmailAddress("niwupeta@p33.org", "Personal"), EmailAddress("yave@p33.org", "Personal"),
                            EmailAddress("kiluci@p33.org", "Other"))
            ))
        }
    }

    override fun deleteContact(contactId: Int): Single<String> {
        return Single.create { received: SingleEmitter<String> ->
            received.onSuccess("Contact Deleted Successfully")
        }
    }

    override fun updateContact(contact: Contact): Single<Contact> {
        return Single.create { received: SingleEmitter<Contact> ->
            received.onSuccess(contact)
        }
    }

    override fun lookForContacts(query: String): Single<List<Contact>> {
        return Single.create { received: SingleEmitter<List<Contact>> ->
            received.onSuccess(Arrays.asList(
                    Contact(
                            profilePic = ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                            name = "Abdel Rahman Abdel Kader",
                            dateOfBirth = "20 Feb 1996",
                            phoneNumbers = RealmList<PhoneNumber>(PhoneNumber("+201127400311", "Work"), PhoneNumber("01127400311", "Other"),
                                    PhoneNumber("55525000", "School")),
                            defaultPhoneNumber = "01127400311",
                            addresses = RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            emailAddresses = RealmList<EmailAddress>(EmailAddress("arakmmis@gmail.com", "Work"), EmailAddress("arakmmis@outlook.com", "Personal"),
                                    EmailAddress("devil-play@hotmail.com", "School"))
                    ),
                    Contact(
                            profilePic = ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)),
                            name = "Mohamed Ahmed",
                            dateOfBirth = "1 Jan 1995",
                            phoneNumbers = RealmList<PhoneNumber>(PhoneNumber("01127498172", "Other"), PhoneNumber("+20191827111", "Home"),
                                    PhoneNumber("98021789042", "Home")),
                            defaultPhoneNumber = "98021789042",
                            addresses = RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str., Alexandria, Egypt", "Home")),
                            emailAddresses = RealmList<EmailAddress>(EmailAddress("worube@p33.org", "Work"), EmailAddress("wabinizo@p33.org", "Other"))
                    ),
                    Contact(
                            4,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Islam El Desoky",
                            "1 Aug 1995",
                            RealmList<PhoneNumber>(PhoneNumber("+98162874962", "Work"), PhoneNumber("0507791448", "Home")),
                            "0507791448",
                            RealmList<Address>(Address("6 Omar Bin El 5atab", "Home"), Address("2768 Peck Street", "Other"),
                                    Address("2090 Briarwood Drive", "Work")),
                            RealmList<EmailAddress>(EmailAddress("tacoxubimu@p33.org", "Work"), EmailAddress("jukara@p33.org", "School"),
                                    EmailAddress("niwupeta@p33.org", "Personal"), EmailAddress("yave@p33.org", "Personal"),
                                    EmailAddress("kiluci@p33.org", "Other"))
                    ),
                    Contact(
                            7,
                            ByteArrayUtil.fromBitmap(BitmapFactory.decodeResource(App.instance?.resources, R.drawable.placeholder_add_profile_pic)), "Alaa Mostafa",
                            "1 Jan 1995",
                            RealmList<PhoneNumber>(PhoneNumber("+1-518-555-0144", "Home")),
                            "+1-518-555-0144",
                            RealmList<Address>(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            RealmList<EmailAddress>(EmailAddress("tufakiz@p33.org", "Personal"))
                    )
            ))
        }
    }
}
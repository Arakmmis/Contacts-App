package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import io.reactivex.SingleEmitter
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import java.util.*

/**
 * Created by arakm on 10/4/2017.
 */
class TestContactsRepo : ContactsManager {

    override fun getContacts(): Single<List<Contact>> {
        return Single.create { received: SingleEmitter<List<Contact>> ->
            received.onSuccess(Arrays.asList(
                    Contact(
                            id = "1",
                            name = "Abdel Rahman Abdel Kader",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("+201127400311", "Work"), PhoneNumber("01127400311", "Other"),
                                    PhoneNumber("55525000", "School")),
                            defaultPhoneNumber = "01127400311",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            emailAddresses = Arrays.asList(EmailAddress("arakmmis@gmail.com", "Work"), EmailAddress("arakmmis@outlook.com", "Personal"),
                                    EmailAddress("devil-play@hotmail.com", "School"))
                    ),
                    Contact(
                            id = "2",
                            name = "Mohamed Ahmed",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("01127498172", "Other"), PhoneNumber("+20191827111", "Home"),
                                    PhoneNumber("98021789042", "Home")),
                            defaultPhoneNumber = "98021789042",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str., Alexandria, Egypt", "Home")),
                            emailAddresses = Arrays.asList(EmailAddress("worube@p33.org", "Work"), EmailAddress("wabinizo@p33.org", "Other"))
                    ),
                    Contact(
                            id = "3",
                            name = "Abdel Rahman El Ghamry",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("+223896400311", "Work"), PhoneNumber("0192180123", "Work"),
                                    PhoneNumber("92361729836", "Work")),
                            defaultPhoneNumber = "0192180123",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab, El Agamy", "Work"), Address("1190 Pine Garden Lane", "Home")),
                            emailAddresses = Arrays.asList(EmailAddress("sadekirepe@p33.org", "Personal"))
                    ),
                    Contact(
                            id = "4",
                            name = "Islam El Desoky",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("+98162874962", "Work"), PhoneNumber("0507791448", "Home")),
                            defaultPhoneNumber = "0507791448",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab", "Home"), Address("2768 Peck Street", "Other"),
                                    Address("2090 Briarwood Drive", "Work")),
                            emailAddresses = Arrays.asList(EmailAddress("tacoxubimu@p33.org", "Work"), EmailAddress("jukara@p33.org", "School"),
                                    EmailAddress("niwupeta@p33.org", "Personal"), EmailAddress("yave@p33.org", "Personal"),
                                    EmailAddress("kiluci@p33.org", "Other"))
                    ),
                    Contact(
                            id = "5",
                            name = "Doha Mohammed Ammar",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("01119868731", "Home")),
                            defaultPhoneNumber = "01119868731",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Work"),
                                    Address("4203 Flanigan Oaks Drive", "School"), Address("106 Lauren Drive", "Home"), Address("4975 Woodbridge Lane", "Home")),
                            emailAddresses = Arrays.asList(EmailAddress("cocarimodi@p33.org", "Personal"))
                    ),
                    Contact(
                            id = "6",
                            name = "Sohaila Mohamed Ahmed Al Alem",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("+1-518-555-0144", "Mobile"), PhoneNumber("518-555-0144", "Other"),
                                    PhoneNumber("518-555-0111", "Mobile"), PhoneNumber("+1-202-555-0163", "Work"), PhoneNumber("202-555-0163", "Work")),
                            defaultPhoneNumber = "518-555-0144",
                            addresses = Arrays.asList(),
                            emailAddresses = Arrays.asList(EmailAddress("vometiroye@p33.org", "Personal"), EmailAddress("donejaneko@p33.org", "Work"))
                    ),
                    Contact(
                            id = "7",
                            name = "Alaa Mostafa",
                            profilePic = "",
                            phoneNumbers = Arrays.asList(PhoneNumber("+1-518-555-0144", "Home")),
                            defaultPhoneNumber = "+1-518-555-0144",
                            addresses = Arrays.asList(Address("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "Home")),
                            emailAddresses = Arrays.asList(EmailAddress("tufakiz@p33.org", "Personal"))
                    )
            ))
        }
    }
}
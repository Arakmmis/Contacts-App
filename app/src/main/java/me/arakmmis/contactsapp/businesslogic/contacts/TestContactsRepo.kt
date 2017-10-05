package me.arakmmis.contactsapp.businesslogic.contacts

import io.reactivex.Single
import io.reactivex.SingleEmitter
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
                            phoneNumbers = Arrays.asList("+201127400311", "01127400311", "55525000"),
                            defaultPhoneNumber = "01127400311",
                            addresses = Arrays.asList("6 Omar Bin El 5atab, El Agamy, El Mror Str."),
                            emailAddresses = Arrays.asList("arakmmis@gmail.com", "arakmmis@outlook.com", "devil-play@hotmail.com")
                    ),
                    Contact(
                            id = "2",
                            name = "Mohamed Ahmed",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("01127498172", "+20191827111", "98021789042"),
                            defaultPhoneNumber = "98021789042",
                            addresses = Arrays.asList("6 Omar Bin El 5atab, El Agamy, El Mror Str., Alexandria, Egypt"),
                            emailAddresses = Arrays.asList("worube@p33.org", "wabinizo@p33.org")
                    ),
                    Contact(
                            id = "3",
                            name = "Abdel Rahman El Ghamry",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("+223896400311", "0192180123", "92361729836"),
                            defaultPhoneNumber = "0192180123",
                            addresses = Arrays.asList("6 Omar Bin El 5atab, El Agamy", "1190 Pine Garden Lane"),
                            emailAddresses = Arrays.asList("sadekirepe@p33.org")
                    ),
                    Contact(
                            id = "4",
                            name = "Islam El Desoky",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("+98162874962", "0507791448"),
                            defaultPhoneNumber = "0507791448",
                            addresses = Arrays.asList("6 Omar Bin El 5atab", "2768 Peck Street", "2090 Briarwood Drive"),
                            emailAddresses = Arrays.asList("tacoxubimu@p33.org", "jukara@p33.org", "niwupeta@p33.org",
                                    "yave@p33.org", "kiluci@p33.org")
                    ),
                    Contact(
                            id = "5",
                            name = "Doha Mohammed Ammar",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("01119868731"),
                            defaultPhoneNumber = "01119868731",
                            addresses = Arrays.asList("6 Omar Bin El 5atab, El Agamy, El Mror Str.", "4203 Flanigan Oaks Drive", "106 Lauren Drive", "4975 Woodbridge Lane"),
                            emailAddresses = Arrays.asList("cocarimodi@p33.org")
                    ),
                    Contact(
                            id = "6",
                            name = "Sohaila Mohamed Ahmed Al Alem",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("+1-518-555-0144", "518-555-0144", "518-555-0111", "+1-202-555-0163", "202-555-0163"),
                            defaultPhoneNumber = "518-555-0144",
                            addresses = Arrays.asList(),
                            emailAddresses = Arrays.asList("vometiroye@p33.org", "donejaneko@p33.org")
                    ),
                    Contact(
                            id = "7",
                            name = "Alaa Mostafa",
                            profilePic = "",
                            phoneNumbers = Arrays.asList("+1-518-555-0144", "+1-225-555-0194", "225-555-0128", "225-555-0115",
                                    "+1-225-555-0195", "+1-225-555-0128", "225-555-0195"),
                            defaultPhoneNumber = "+1-518-555-0144",
                            addresses = Arrays.asList("6 Omar Bin El 5atab, El Agamy, El Mror Str."),
                            emailAddresses = Arrays.asList("tufakiz@p33.org")
                    )
            ))
        }
    }
}
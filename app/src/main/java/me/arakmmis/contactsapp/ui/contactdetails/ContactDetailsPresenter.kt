package me.arakmmis.contactsapp.ui.contactdetails

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.TestContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.mvpcontracts.ContactDetailsContract

class ContactDetailsPresenter(val contactDetailsView: ContactDetailsContract.ContactDetailsView, val contactId: Int) : ContactDetailsContract.ContactDetailsPresenter {

    private val contactsManager: ContactsManager = TestContactsRepo()

    init {
        getContactData(contactId)
    }

    private fun getContactData(contactId: Int) {
        contactsManager.getContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Contact> {
                    override fun onSuccess(t: Contact) {
                        contactDetailsView.setContactData(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("CDP: getContact", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }
}
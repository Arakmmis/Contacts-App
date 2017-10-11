package me.arakmmis.contactsapp.ui.contactdetails

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.mvpcontracts.ContactDetailsContract

class ContactDetailsPresenter(val contactDetailsView: ContactDetailsContract.ContactDetailsView, val contactId: Int) : ContactDetailsContract.ContactDetailsPresenter {

    private val contactsManager: ContactsManager = ContactsRepo()

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

    override fun deleteContact(contactId: Int) {
        contactsManager.deleteContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<String> {
                    override fun onError(e: Throwable) {
                        Log.e("CDP: deleteContact", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: String) {
                        contactDetailsView.toast(t)
                        contactDetailsView.navigateToHome()
                    }
                })
    }
}
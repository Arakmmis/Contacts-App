package me.arakmmis.contactsapp.ui.home

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.mvpcontracts.HomeContract

class HomePresenter(val homeView: HomeContract.HomeView) : HomeContract.HomePresenter {

    private val contactsManager: ContactsManager = ContactsRepo()

    init {
        getContacts()
    }

    private fun getContacts() {
        contactsManager.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleObserver<List<Contact>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("HP: getContacts", e.message)
                    }

                    override fun onSuccess(contacts: List<Contact>) {
                        homeView.setContacts(contacts)
                    }
                })
    }
}
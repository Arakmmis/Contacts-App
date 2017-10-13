package me.arakmmis.contactsapp.ui.home

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsManager
import me.arakmmis.contactsapp.businesslogic.contacts.ContactsRepo
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.mvpcontracts.HomeContract
import me.arakmmis.contactsapp.utils.App

class HomePresenter(val homeView: HomeContract.HomeView) : HomeContract.HomePresenter {

    private val contactsManager: ContactsManager = ContactsRepo()

    init {
        getContacts()
    }

    private fun getContacts() {
        contactsManager.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Contact>> {
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

    override fun lookFor(query: String) {
        contactsManager.lookForContacts(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Contact>> {
                    override fun onError(e: Throwable) {
                        Log.e("HP: search", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(result: List<Contact>) {
                        if (result.isEmpty()) {
                            homeView.toast(App.instance?.getString(R.string.empty_search_result_txt)!!)
                        } else {
                            homeView.setContacts(result)
                        }
                    }
                })
    }
}
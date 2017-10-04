package me.arakmmis.contactsapp.ui.home

import me.arakmmis.contactsapp.mvpcontracts.HomeContract

/**
 * Created by arakm on 10/4/2017.
 */
class HomePresenter(val homeView: HomeContract.HomeView) : HomeContract.HomePresenter {

    init {
        getContacts()
    }

    private fun getContacts() {

    }
}
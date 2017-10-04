package me.arakmmis.contactsapp.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.mvpcontracts.HomeContract

/**
 * Created by arakm on 10/4/2017.
 */
class HomeActivity : AppCompatActivity(), HomeContract.HomeView {

    lateinit var presenter: HomeContract.HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = HomePresenter(this)
    }

    fun openContactDetails(v: View) {

    }
}

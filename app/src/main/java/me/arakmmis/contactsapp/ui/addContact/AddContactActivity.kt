package me.arakmmis.contactsapp.ui.addContact

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract

/**
 * Created by arakm on 10/5/2017.
 */
class AddContactActivity : AppCompatActivity(), AddContactContract.AddContactView {

    lateinit var presenter: AddContactContract.AddContactPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_activity)

        presenter = AddContactPresenter(this)
    }
}
package me.arakmmis.contactsapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_activity.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.mvpcontracts.HomeContract
import me.arakmmis.contactsapp.ui.contactdetails.ContactDetailsActivity
import me.arakmmis.contactsapp.ui.home.adapter.ContactsAdapter
import me.arakmmis.contactsapp.utils.Callback
import me.arakmmis.contactsapp.utils.Const

/**
 * Created by arakm on 10/4/2017.
 */
class HomeActivity : AppCompatActivity(), HomeContract.HomeView, Callback<Contact> {

    lateinit var presenter: HomeContract.HomePresenter

    var adapterContacts: ContactsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = HomePresenter(this)

        rv_contacts.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
    }

    override fun setContacts(contacts: List<Contact>) {
        if (adapterContacts == null) {
            adapterContacts = ContactsAdapter(this, contacts)
            rv_contacts.adapter = adapterContacts
        } else {
            adapterContacts?.setData(contacts)
        }
    }

    override fun onClick(item: Contact) {
        val intent = Intent(this@HomeActivity, ContactDetailsActivity::class.java)
        intent.putExtra(Const.CONTACT_KEY, item)
        startActivity(intent)
    }
}

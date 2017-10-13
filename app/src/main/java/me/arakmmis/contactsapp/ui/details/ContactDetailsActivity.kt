package me.arakmmis.contactsapp.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.contact_details_activity.*
import kotlinx.android.synthetic.main.contact_details_dialog_delete_contact.view.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.mvpcontracts.ContactDetailsContract
import me.arakmmis.contactsapp.ui.details.adapter.DetailsAdapter
import me.arakmmis.contactsapp.ui.edit.EditContactActivity
import me.arakmmis.contactsapp.ui.home.HomeActivity
import me.arakmmis.contactsapp.utils.Cache
import me.arakmmis.contactsapp.utils.Const

class ContactDetailsActivity : AppCompatActivity(), ContactDetailsContract.ContactDetailsView {

    lateinit var presenter: ContactDetailsContract.ContactDetailsPresenter

    private val contactId by lazy {
        Cache.getTempUser() ?: intent.extras.getInt(Const.CONTACT_ID_KEY)
    }

    companion object {
        fun start(context: Context, contactId: Int) {
            val intent = Intent(context, ContactDetailsActivity::class.java)
            intent.putExtra(Const.CONTACT_ID_KEY, contactId)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_details_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = ContactDetailsPresenter(this, contactId)

        initUI()
    }

    private fun initUI() {
        rv_phone_numbers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_phone_numbers.isNestedScrollingEnabled = false

        rv_email_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_email_addresses.isNestedScrollingEnabled = false
    }

    override fun setContactData(contact: Contact) {
        Glide.with(this@ContactDetailsActivity)
                .load(contact.profilePic)
                .into(iv_contact_pic)

        tv_contact_name.text = contact.name
        tv_birth_date.text = contact.dateOfBirth

        rv_phone_numbers.adapter = DetailsAdapter<PhoneNumber>(R.layout.contact_details_rv_item_phone_number,
                contact.phoneNumbers)
        rv_email_addresses.adapter = DetailsAdapter<EmailAddress>(R.layout.contact_details_rv_item_email_address,
                contact.emailAddresses)

        if (contact.addresses.isEmpty()) {
            rl_addresses.visibility = View.GONE
        } else {
            rv_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rv_addresses.isNestedScrollingEnabled = false
            rv_addresses.adapter = DetailsAdapter<Address>(R.layout.contact_details_rv_item_address,
                    contact.addresses)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_details_menu, menu);

        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete -> {
                openLastCheckDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun openLastCheckDialog() {
        val dialogView = layoutInflater.inflate(R.layout.contact_details_dialog_delete_contact, null)
        val alertDialog = AlertDialog.Builder(this).setView(dialogView).create()

        dialogView.ok.setOnClickListener { _ ->
            presenter.deleteContact(contactId)
            alertDialog.dismiss()
        }

        dialogView.cancel.setOnClickListener { _ ->
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun toast(message: String) {
        Toast.makeText(this@ContactDetailsActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        HomeActivity.start(this@ContactDetailsActivity)
        finish()
    }

    fun editContact(v: View) {
        EditContactActivity.start(this@ContactDetailsActivity, contactId)
        Cache.setTempUser(contactId)
        finish()
    }

    override fun onBackPressed() {
        navigateToHome()
    }
}
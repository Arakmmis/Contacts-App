package me.arakmmis.contactsapp.ui.home

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.home_activity.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.customlisteners.Callback
import me.arakmmis.contactsapp.mvpcontracts.HomeContract
import me.arakmmis.contactsapp.ui.add.AddContactActivity
import me.arakmmis.contactsapp.ui.details.ContactDetailsActivity
import me.arakmmis.contactsapp.ui.home.adapter.ContactsAdapter
import me.arakmmis.contactsapp.utils.Cache
import java.util.concurrent.TimeUnit

/**
 * Created by arakm on 10/4/2017.
 */
class HomeActivity : AppCompatActivity(), HomeContract.HomeView, Callback<Contact> {

    private lateinit var presenter: HomeContract.HomePresenter
    private var adapterContacts: ContactsAdapter? = null
    private lateinit var searchView: SearchView

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = HomePresenter(this)

        rv_contacts.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
    }

    override fun setContacts(contacts: List<Contact>) {
        if (contacts.isEmpty()) {
            Toast.makeText(this@HomeActivity, getString(R.string.no_contacts_to_display), Toast.LENGTH_SHORT).show()
        } else {
            if (adapterContacts == null) {
                adapterContacts = ContactsAdapter(this, contacts)
                rv_contacts.adapter = adapterContacts
            } else {
                adapterContacts?.setData(contacts)
            }
        }
    }

    override fun onClick(item: Contact) {
        ContactDetailsActivity.start(this@HomeActivity, item.id)
    }

    fun addContact(v: View) {
        AddContactActivity.start(this@HomeActivity)
    }

    override fun onStart() {
        super.onStart()
        Cache.removeTempUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_query_hint)

        setQueryListenerForSearchView()

        return true
    }

    private fun setQueryListenerForSearchView() {
        Observable.create(ObservableOnSubscribe<String> { e ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = false

                override fun onQueryTextChange(newText: String): Boolean {
                    e.onNext(newText)
                    return false
                }
            })
        })
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onNext(t: String) {
                        presenter.lookFor(t)
                    }

                    override fun onComplete() {
                        Log.d("HA: search", "Done")
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("HA: search", e.message)
                    }
                })
    }

    override fun toast(message: String) {
        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
    }
}

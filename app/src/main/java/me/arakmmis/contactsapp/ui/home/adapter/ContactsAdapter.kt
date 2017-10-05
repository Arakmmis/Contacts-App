package me.arakmmis.contactsapp.ui.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.contacts.Contact
import me.arakmmis.contactsapp.utils.Callback

class ContactsAdapter(val listener: Callback<Contact>, var contacts: List<Contact>) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_rv_item_contact, parent, false)
        return ContactViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder?, position: Int) {
        holder?.setData(contact = contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    fun setData(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }
}
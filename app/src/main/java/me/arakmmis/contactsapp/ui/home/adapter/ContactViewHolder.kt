package me.arakmmis.contactsapp.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.home_rv_item_contact.view.*
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.customlisteners.Callback


class ContactViewHolder(itemView: View?, listener: Callback<Contact>) : RecyclerView.ViewHolder(itemView) {

    lateinit var contact: Contact

    init {
        itemView?.rl_contact_details?.setOnClickListener { _ -> listener.onClick(contact) }

        itemView?.iv_call?.setOnClickListener { _ ->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + contact.defaultPhoneNumber)
            startActivity(itemView.context, intent, null)
        }
    }

    fun setData(contact: Contact) {
        this.contact = contact

        Glide.with(itemView?.context)
                .load(contact.profilePic)
                .into(itemView?.iv_contact_pic)

        itemView.tv_contact_name.text = contact.name
        itemView.tv_contact_default_number.text = contact.defaultPhoneNumber
    }
}
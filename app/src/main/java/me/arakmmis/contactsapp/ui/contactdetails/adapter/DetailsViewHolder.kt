package me.arakmmis.contactsapp.ui.contactdetails.adapter

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.contact_details_rv_item_address.view.*
import kotlinx.android.synthetic.main.contact_details_rv_item_email_address.view.*
import kotlinx.android.synthetic.main.contact_details_rv_item_phone_number.view.*
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber


class DetailsViewHolder<T>(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    var t: T? = null

    fun setData(t: T) {
        when (t) {
            is PhoneNumber -> initPNView(t)
            is EmailAddress -> initEAView(t)
            is Address -> initAddressView(t)
            else -> Toast.makeText(itemView?.context, "Error with T", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initPNView(phoneNumber: PhoneNumber) {
        itemView?.tv_phone_number?.text = phoneNumber.number
        itemView?.tv_phone_number_type?.text = phoneNumber.type

        itemView?.ll_phone_number_details?.setOnClickListener { _ ->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + phoneNumber.number)
            ContextCompat.startActivity(itemView.context, intent, null)
        }

        itemView?.ll_phone_number_details?.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                // Copy To Clipboard
                Toast.makeText(itemView.context, "Phone Number Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        itemView?.iv_message?.setOnClickListener { _ ->
            startActivity(itemView.context, Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber.number, null)), null)
        }
    }

    private fun initEAView(emailAddress: EmailAddress) {
        itemView?.tv_email_address?.text = emailAddress.emailAddress
        itemView?.tv_email_address_type?.text = emailAddress.type

        itemView?.tv_email_address?.setOnClickListener { _ ->
            // Copy To Clipboard
            Toast.makeText(itemView.context, "Email Address Copied to Clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initAddressView(address: Address) {
        itemView?.tv_address?.text = address.address
        itemView?.tv_address_type?.text = address.type

        itemView?.tv_address?.setOnClickListener { _ ->
            // Copy To Clipboard
            Toast.makeText(itemView.context, "Address Copied to Clipboard!", Toast.LENGTH_SHORT).show()
        }
    }
}
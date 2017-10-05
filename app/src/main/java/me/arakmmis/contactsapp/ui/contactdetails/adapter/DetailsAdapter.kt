package me.arakmmis.contactsapp.ui.contactdetails.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class DetailsAdapter<T>(val layout: Int, private val t: List<T>) : RecyclerView.Adapter<DetailsViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DetailsViewHolder<T> {
        val view = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return DetailsViewHolder<T>(view)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder<T>?, position: Int) {
        holder?.setData(t = t[position])
    }

    override fun getItemCount(): Int = t.size
}
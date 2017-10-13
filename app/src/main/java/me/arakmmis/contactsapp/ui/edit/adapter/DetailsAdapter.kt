package me.arakmmis.contactsapp.ui.edit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.arakmmis.contactsapp.customlisteners.Callback
import me.arakmmis.contactsapp.customlisteners.EditContactCallback

class DetailsAdapter<T>(val layout: Int, private var t: List<T>, val callback: EditContactCallback<T>) : RecyclerView.Adapter<DetailsViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DetailsViewHolder<T> {
        val view = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return DetailsViewHolder<T>(view, callback)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder<T>?, position: Int) {
        holder?.setData(t = t[position])
    }

    override fun getItemCount(): Int = t.size

    fun setData(data: List<T>) {
        t = data
        notifyDataSetChanged()
    }
}
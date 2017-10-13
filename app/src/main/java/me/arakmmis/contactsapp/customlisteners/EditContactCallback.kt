package me.arakmmis.contactsapp.customlisteners

interface EditContactCallback<in T> {

    fun onEditClicked(item: T, position: Int)

    fun onDeleteClicked(item: T)
}
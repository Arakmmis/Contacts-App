package me.arakmmis.contactsapp.customlisteners

interface Callback<in T> {

    fun onClick(item: T)
}
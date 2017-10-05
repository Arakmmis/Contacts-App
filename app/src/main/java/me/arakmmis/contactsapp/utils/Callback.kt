package me.arakmmis.contactsapp.utils

interface Callback<in T> {

    fun onClick(item: T)
}
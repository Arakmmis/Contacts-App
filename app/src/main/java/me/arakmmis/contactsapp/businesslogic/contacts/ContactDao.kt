package me.arakmmis.contactsapp.businesslogic.contacts

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Single
import me.arakmmis.contactsapp.businesslogic.models.Contact

interface ContactDao {

    @Query("SELECT * FROM Contact")
    fun getAll(): Single<List<Contact>>

    @Query("SELECT * FROM Contact WHERE name LIKE :name")
    fun findByName(name: String): Single<List<Contact>>

    @Query("SELECT * FROM Contact WHERE number LIKE :number")
    fun findByNumber(number: String): Single<List<Contact>>

    @Query("SELECT * FROM Contact WHERE email LIKE :email")
    fun findByEmail(email: String): Single<List<Contact>>

    @Insert
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Update
    fun update(contact: Contact)
}
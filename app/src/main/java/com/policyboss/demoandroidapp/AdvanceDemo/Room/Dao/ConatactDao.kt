package com.policyboss.demoandroidapp.AdvanceDemo.Room.Dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

import com.policyboss.demoandroidapp.AdvanceDemo.Room.Entity.Contact

@Dao
interface ConatactDao {


    @Insert (onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertContact(contact: Contact)


    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)


    @Query("select * from contact" )
     fun getContact() : LiveData<List<Contact>> // Since we use Live data its bydefault execute in background, no need suspend fun


    @Query("select * from contact" )
    suspend fun getContact1() : List<Contact> // Since we use Live data its bydefault execute in background, no need suspend fun

}
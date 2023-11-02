package com.policyboss.demoandroidapp.AdvanceDemo.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contact")
data class Contact (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String,
    val mobile : String,
    val address : String,
    val createDate : Date
   )

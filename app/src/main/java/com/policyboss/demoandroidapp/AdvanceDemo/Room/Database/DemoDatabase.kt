package com.policyboss.demoandroidapp.AdvanceDemo.Room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.Dao.QuoteDao
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.QuoteResponse.QuoteEntity


import com.policyboss.demoandroidapp.AdvanceDemo.Room.Convertors.Convertors
import com.policyboss.demoandroidapp.AdvanceDemo.Room.Dao.ConatactDao
import com.policyboss.demoandroidapp.AdvanceDemo.Room.Entity.Contact
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.LoginEntity
import com.policyboss.demoandroidapp.MVVMDemo.Dao.ConstantDao
import com.policyboss.demoandroidapp.MVVMDemo.Dao.LoginDao

import com.policyboss.demoandroidapp.MVVMDemo.Data.DashboardData.ConstantEntity


/***************** Defination****************************
//volatile : --> property as volatile, meaning that writes to this field are immediately made visible to other threads.


 we have used Singleton pattern to Create RoomDatabase INSTANCE
 **********************************************/


@Database(entities = [LoginEntity::class, Contact::class, QuoteEntity::class, ConstantEntity::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class DemoDatabase : RoomDatabase() {


    abstract fun loginDao() : LoginDao

    abstract fun contactDao() : ConatactDao

    abstract fun  quoteDao() : QuoteDao

    abstract fun  constantDao() : ConstantDao

    companion object{

        @Volatile
        private var INSTANCE: DemoDatabase? = null       // Volatile : here any changes in "Instance" are immediately made visible to all threads

        fun  getDatabase(context : Context) : DemoDatabase {

            if(INSTANCE == null){

                synchronized(this){                                         // Note synchronized : due to multithread it may
                                                                                    // chance  create multiple instance of Room Database.
                    INSTANCE = Room.databaseBuilder(context.applicationContext,    // Synchronize lock the particular instance.
                        DemoDatabase::class.java, "DemoDB")
                       .fallbackToDestructiveMigration()
                        .build()
                }

            }

            return INSTANCE!!

        }
    }


    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        // From version 1 to version 2
        override fun migrate(database: SupportSQLiteDatabase) {
            // Remove the table
            database.execSQL("DROP TABLE my_table") // Use the right table name

            // OR: We could update it, by using an ALTER query

            // OR: If needed, we can create the table again with the required settings
            // database.execSQL("CREATE TABLE IF NOT EXISTS my_table (id INTEGER, PRIMARY KEY(id), ...)")
        }
    }

}
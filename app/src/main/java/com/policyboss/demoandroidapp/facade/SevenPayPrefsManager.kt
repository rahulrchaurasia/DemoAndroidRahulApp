package com.policyboss.demoandroidapp.facade

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.policyboss.demoandroidapp.response.Login.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SevenPayPrefsManager @Inject constructor(@ApplicationContext context : Context) {

    private val TAG: String = SevenPayPrefsManager::class.java.simpleName

    private val SHARED_PREF: String = "sevenpay_distributer1"
    private val USER: String = "user"
    private val TOKEN: String = "token"

    private var sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPref.edit()
    }

    //region user

    fun saveUser(user: User?) {

        Log.d(TAG, "preference: ${user?.firstName}")

        user?.let {
            saveToken(it.token)
            getEditor().putString(USER, Gson().toJson(user)).apply()
        }
    }

    fun getUser(): User? {
        return Gson().fromJson(sharedPref.getString(USER, ""), User::class.java) ?: null
    }

    fun saveToken(token: String) {
        getEditor().putString(TOKEN, token).apply()
    }

    fun getToken() = sharedPref.getString(TOKEN, "")

    //endregion
}
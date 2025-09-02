package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val PREF_NAME = "MyAppPref"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_IS_FIRST_LOGIN = "isFirstLogin"
    private val KEY_USER_ID = "user_id"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setLogin(isLoggedIn: Boolean, userId: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .putBoolean(KEY_IS_FIRST_LOGIN, true)
            .putString(KEY_USER_ID, userId)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun isFirstLogin(): Boolean {
        return prefs.getBoolean(KEY_IS_FIRST_LOGIN, false)
    }

    fun setFirstLoginDone() {
        prefs.edit().putBoolean(KEY_IS_FIRST_LOGIN, false).apply()
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}

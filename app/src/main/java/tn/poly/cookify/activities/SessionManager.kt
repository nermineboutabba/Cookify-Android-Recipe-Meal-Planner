package tn.poly.cookify.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("cookify_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_LOGGED_IN = "logged_in"
    }

    fun saveLogin(userId: Int) {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
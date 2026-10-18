package com.creativeblisslabs.tiffinmanagement.data.storage

import android.content.Context
import android.content.SharedPreferences

actual object SessionStorage {
    private const val PREFS_NAME = "tiffin_session_prefs"
    private const val KEY_EMAIL = "auth_user_email"
    private const val KEY_FULL_NAME = "auth_user_full_name"
    private const val KEY_ROLE = "auth_user_role"
    private const val KEY_BUSINESS_NAME = "auth_user_business_name"
    private const val KEY_PHONE = "auth_user_phone"
    private const val KEY_PROVIDER = "auth_user_provider"

    private var prefs: SharedPreferences? = null

    actual fun init(context: Any?) {
        if (context is Context) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    actual fun saveSession(
        email: String,
        fullName: String,
        role: String,
        businessName: String?,
        phone: String?,
        provider: String
    ) {
        prefs?.edit()?.apply {
            putString(KEY_EMAIL, email)
            putString(KEY_FULL_NAME, fullName)
            putString(KEY_ROLE, role)
            putString(KEY_BUSINESS_NAME, businessName)
            putString(KEY_PHONE, phone)
            putString(KEY_PROVIDER, provider)
            apply()
        }
    }

    actual fun getSavedEmail(): String? = prefs?.getString(KEY_EMAIL, null)

    actual fun getSavedFullName(): String? = prefs?.getString(KEY_FULL_NAME, null)

    actual fun getSavedRole(): String? = prefs?.getString(KEY_ROLE, null)

    actual fun getSavedBusinessName(): String? = prefs?.getString(KEY_BUSINESS_NAME, null)

    actual fun getSavedPhone(): String? = prefs?.getString(KEY_PHONE, null)

    actual fun getSavedProvider(): String? = prefs?.getString(KEY_PROVIDER, null)

    actual fun clearSession() {
        prefs?.edit()?.clear()?.apply()
    }
}

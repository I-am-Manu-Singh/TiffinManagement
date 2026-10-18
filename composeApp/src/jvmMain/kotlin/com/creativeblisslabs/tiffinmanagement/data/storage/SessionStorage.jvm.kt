package com.creativeblisslabs.tiffinmanagement.data.storage

actual object SessionStorage {
    private var email: String? = null
    private var fullName: String? = null
    private var role: String? = null
    private var businessName: String? = null
    private var phone: String? = null
    private var provider: String? = null

    actual fun init(context: Any?) {}

    actual fun saveSession(
        email: String,
        fullName: String,
        role: String,
        businessName: String?,
        phone: String?,
        provider: String
    ) {
        this.email = email
        this.fullName = fullName
        this.role = role
        this.businessName = businessName
        this.phone = phone
        this.provider = provider
    }

    actual fun getSavedEmail(): String? = email
    actual fun getSavedFullName(): String? = fullName
    actual fun getSavedRole(): String? = role
    actual fun getSavedBusinessName(): String? = businessName
    actual fun getSavedPhone(): String? = phone
    actual fun getSavedProvider(): String? = provider

    actual fun clearSession() {
        email = null
        fullName = null
        role = null
        businessName = null
        phone = null
        provider = null
    }
}

package com.creativeblisslabs.tiffinmanagement.data.storage

expect object SessionStorage {
    fun init(context: Any? = null)
    fun saveSession(
        email: String,
        fullName: String,
        role: String,
        businessName: String?,
        phone: String?,
        provider: String
    )
    fun getSavedEmail(): String?
    fun getSavedFullName(): String?
    fun getSavedRole(): String?
    fun getSavedBusinessName(): String?
    fun getSavedPhone(): String?
    fun getSavedProvider(): String?
    fun clearSession()
}

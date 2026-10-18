package com.creativeblisslabs.tiffinmanagement.config

/**
 * App Configuration constants.
 * Sensitive values are mirrored from .env.
 */
object AppConfig {
    const val SUPABASE_URL: String = "https://viztueivsindmvtnactv.supabase.co"
    const val SUPABASE_ANON_KEY: String = "sb_publishable_716jtX_GjEH19Jkq7qatUA__2dtrRIk"
    const val GOOGLE_WEB_CLIENT_ID: String = "1322063139-7l4ot4kmihhhsn4uptnknsilceu5o8n8.apps.googleusercontent.com"
    const val AUTH_REDIRECT_SCHEME: String = "tiffinmanagement"
    const val AUTH_REDIRECT_HOST: String = "login-callback"
    const val AUTH_REDIRECT_URL: String = "tiffinmanagement://login-callback"
}

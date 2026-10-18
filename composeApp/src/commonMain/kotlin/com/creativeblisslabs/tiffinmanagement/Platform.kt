package com.creativeblisslabs.tiffinmanagement

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
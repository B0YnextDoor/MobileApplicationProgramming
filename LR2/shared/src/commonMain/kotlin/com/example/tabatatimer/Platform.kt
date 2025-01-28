package com.example.tabatatimer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
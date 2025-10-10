package org.notes.multi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
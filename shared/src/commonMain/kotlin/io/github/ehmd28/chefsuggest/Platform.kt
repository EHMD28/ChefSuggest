package io.github.ehmd28.chefsuggest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
package io.github.ehmd28.chefsuggest.chefsuggest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
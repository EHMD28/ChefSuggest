package io.github.ehmd28.chefsuggest.chefsuggest

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
package com.example.chefsuggest

import io.github.ehmd28.chefsuggest.chefsuggest.Platform

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
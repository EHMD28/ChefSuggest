package io.github.ehmd28.chefsuggest.multiplatform

import android.os.Build
import io.github.ehmd28.chefsuggest.chefsuggest.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
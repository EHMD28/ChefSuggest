package chefsuggest.utils

import java.awt.Color
import java.awt.Font

object AppConstants {
    private const val UNIX_PATH = "~/.config/ChefSuggest"
    // TODO: Change to work on Windows as well
    private const val WINDOWS_PATH = "Users\\Username\\AppData"

    fun getAppConfigPath() : String {
        val osName = System.getProperty("os.name").lowercase()
        val osType = when {
            "windows" in osName -> "WINDOWS"
            listOf("mac", "nix", "sunos", "solaris", "bsd").any { it in osName } -> "UNIX"
            else -> ""
        }
        return if (osType == "UNIX") {
            UNIX_PATH
        } else {
            WINDOWS_PATH
        }
    }
}

object Palette {
    val PRIMARY_BG = Color(0xED8FFF)
    val SECONDARY_BG = Color(0xBF00FF)

    fun getPrimaryFontWithSize(fontSize: Int) : Font {
        return Font("Arial", Font.PLAIN, fontSize)
    }
}

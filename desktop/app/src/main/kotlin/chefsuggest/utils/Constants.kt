package chefsuggest.utils

import java.awt.Color
import java.awt.Font
import java.nio.file.Path
import kotlin.io.path.Path

object AppPaths {
    /**
     * Returns a path to the directory containing all Chef Suggest program data.
     * UNIX: /home/username/.ChefSuggest/
     * WINDOWS: ???
     */
    fun getBasePath() : Path {
        val home = System.getProperty("user.home")
        return Path("$home/.ChefSuggest")
    }

    /**
     * Returns a path to the file where all meal data is stored.
     */
    fun getMealsPath() : Path {
        return getBasePath().resolve("Meals.tsv")
    }

    /**
     * Returns a path to directory containing saved meal configurations.
     */
    fun getConfigsPath() : Path {
        return getBasePath().resolve("SavedConfigs")
    }
}

object Palette {
    val PRIMARY_BG = Color(0xED8FFF)
    val SECONDARY_BG = Color(0xBF00FF)

    fun getPrimaryFontWithSize(fontSize: Int) : Font {
        return Font("Arial", Font.PLAIN, fontSize)
    }
}

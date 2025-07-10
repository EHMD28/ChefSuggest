package chefsuggest.utils

import java.awt.Color
import java.awt.Font
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

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


    /**
     * Creates necessary directories and files for storing user information if the directories do not
     * already exist.
     */
    fun initialize() {
        /* /home/username/.ChefSuggest */
        val basePath = AppPaths.getBasePath()
        if (!basePath.exists()) basePath.createDirectories()
        /* /home/username/.ChefSuggest/Meals.tsv */
        val mealsPath = AppPaths.getMealsPath()
        if (!mealsPath.exists()) mealsPath.createFile()
        /* /home/username/.ChefSuggest/SavedConfigs */
        val configsPath = AppPaths.getConfigsPath()
        if (!configsPath.exists()) configsPath.createDirectory()
    }
}

object Palette {
    val PRIMARY_BG = Color(0xED8FFF)
    val SECONDARY_BG = Color(0xBF00FF)

    fun getPrimaryFontWithSize(fontSize: Int) : Font {
        return Font("Arial", Font.PLAIN, fontSize)
    }
}

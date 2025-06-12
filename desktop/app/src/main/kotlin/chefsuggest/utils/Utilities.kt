package chefsuggest.utils

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

/**
 * Creates necessary directories and files for storing user information if the directories do not
 * already exist.
 */
fun initAppConfigFiles() {
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

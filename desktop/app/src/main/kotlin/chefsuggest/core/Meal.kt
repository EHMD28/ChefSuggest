package chefsuggest.core

data class Meal(var name: String? = null, var tags: MutableList<String>? = null, var recipe: Recipe? = null) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.appendLine("Name: $name")
        stringBuilder.append("Tags: ")
        tags?.forEach {
            stringBuilder.append("$it, ")
        }
        stringBuilder.setLength(stringBuilder.length - 2)
        stringBuilder.append("\n")
        stringBuilder.appendLine(recipe)
        return stringBuilder.toString()
    }
}

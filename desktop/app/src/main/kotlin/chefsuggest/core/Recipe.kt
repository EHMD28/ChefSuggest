package chefsuggest.core

data class Recipe(
    var url: String? = null,
    var ingredients: MutableList<String>? = null,
    var steps: MutableList<String>? = null
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Ingredients: ")
        ingredients?.forEach {
            stringBuilder.append("$it, ")
        }
        stringBuilder.setLength(stringBuilder.length - 2)
        stringBuilder.appendLine()
        stringBuilder.appendLine("Steps: ")
        steps?.forEach {
            stringBuilder.appendLine("\t$it")
        }
        stringBuilder.appendLine()
        return stringBuilder.toString()
    }
}

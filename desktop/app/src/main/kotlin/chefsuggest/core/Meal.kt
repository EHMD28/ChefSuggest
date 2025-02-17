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

    override fun equals(other: Any?): Boolean {
        if (other !is Meal) return false
        var status = false
        return if (this.name != other.name) false
                else if (this.tags != other.tags) false
                else if (this.recipe != other.recipe) false
                else true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (tags?.hashCode() ?: 0)
        result = 31 * result + (recipe?.hashCode() ?: 0)
        return result
    }
}

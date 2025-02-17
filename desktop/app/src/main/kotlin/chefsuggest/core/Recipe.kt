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

    override fun equals(other: Any?): Boolean {
        if (other !is Recipe) return false;
        return if (this.url != other.url) false
        else if (this.ingredients != other.ingredients) false
        else if (this.steps != other.steps) false
        else true
    }

    override fun hashCode(): Int {
        var result = url?.hashCode() ?: 0
        result = 31 * result + (ingredients?.hashCode() ?: 0)
        result = 31 * result + (steps?.hashCode() ?: 0)
        return result
    }
}

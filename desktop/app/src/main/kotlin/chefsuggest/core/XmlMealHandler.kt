package chefsuggest.core

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class XmlMealHandler : DefaultHandler() {
    companion object {
        private var meal: Meal? = null
        private var stringBuilder = StringBuilder()

        private const val NAME_TAG = "name"
        private const val TAGS_TAG = "tags"
        private const val TAG_TAG = "tag"
        private const val RECIPE_TAG = "recipe"
        private const val URL_TAG = "url"
        private const val INGREDIENTS_TAG = "ingredients"
        private const val INGREDIENT_TAG = "ingredient"
        private const val STEPS_TAG = "steps"
        private const val STEP_TAG = "step"
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        stringBuilder.appendRange(ch!!, start, start + length)
    }

    override fun startDocument() {
        meal = Meal();
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        when (qName) {
            NAME_TAG -> stringBuilder.clear()
            TAGS_TAG -> meal?.tags = mutableListOf()
            TAG_TAG -> stringBuilder.clear()
            RECIPE_TAG -> meal?.recipe = Recipe()
            URL_TAG -> stringBuilder.clear()
            INGREDIENTS_TAG -> meal?.recipe?.ingredients = mutableListOf()
            INGREDIENT_TAG -> stringBuilder.clear()
            STEPS_TAG -> meal?.recipe?.steps = mutableListOf()
            STEP_TAG -> stringBuilder.clear()
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        when (qName) {
            NAME_TAG -> meal?.name = stringBuilder.toString()
            TAG_TAG -> meal?.tags?.add(stringBuilder.toString())
            URL_TAG -> meal?.recipe?.url = stringBuilder.toString()
            INGREDIENT_TAG -> meal?.recipe?.ingredients?.add(stringBuilder.toString())
            STEP_TAG -> meal?.recipe?.steps?.add(stringBuilder.toString())
        }
    }

    fun getMeal() : Meal {
        return meal!!
    }
}

package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.RecipeType
import kotlinx.serialization.Serializable

abstract class AbstractRecipeSettings(protected val type: RecipeType)
{
	@Serializable
	data class Recipe(
		val type: String,
		val group: String? = null,
		val pattern: List<String>?,
		val key: Map<String, Item>?,
		val ingredients: List<Item>?,
		val result: Result)
	{
		@Serializable
		data class Item(
			val item: String,
			val tag: String? = null)

		//TODO: this looks different in non crafting recipes
		@Serializable
		data class Result(
			val count: Int? = null,
			val item: String)
	}

	abstract fun serialisable(namespace: String, id: String): Recipe
}

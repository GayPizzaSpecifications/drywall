package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.Blocks
import gay.pizza.foundation.drywall.enums.Items
import gay.pizza.foundation.drywall.enums.RecipeType
import gay.pizza.foundation.drywall.util.ResourcePath

class ShapelessRecipeSettings: AbstractRecipeSettings(RecipeType.SHAPELESS)
{
	private val ingredients: MutableList<ResourcePath> = mutableListOf()
	private var result: ResourcePath? = null
	private var resultCount = 1

	fun ingredient(item: Items): ShapelessRecipeSettings
	{
		return ingredient(item.toPath())
	}

	fun ingredient(blockItem: Blocks): ShapelessRecipeSettings
	{
		return ingredient(blockItem.toPath())
	}

	fun ingredient(itemPath: ResourcePath): ShapelessRecipeSettings
	{
		ingredients.add(itemPath)
		return this
	}

	fun result(count: Int): ShapelessRecipeSettings
	{
		resultCount = count
		return this
	}

	fun result(itemPath: ResourcePath, count: Int = 1): ShapelessRecipeSettings
	{
		result = itemPath
		resultCount = count
		return this
	}

	override fun serialisable(namespace: String, id: String): Recipe
	{
		return Recipe("$type", null,
			null,
			null,
			ingredients.map { Recipe.Item(it.resolve(namespace, id)) },
			Recipe.Result(this.resultCount, result?.resolve(namespace, id) ?: "$namespace:$id")
		)
	}
}

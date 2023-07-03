package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.Blocks
import gay.pizza.foundation.drywall.enums.Items
import gay.pizza.foundation.drywall.enums.RecipeType
import gay.pizza.foundation.drywall.util.ResourcePath

class ShapedRecipeSettings: AbstractRecipeSettings(RecipeType.SHAPED)
{
	private val patternLines: MutableList<String> = mutableListOf()
	private val keys: MutableMap<Char, ResourcePath> = mutableMapOf()
	private var result: ResourcePath? = null
	private var resultCount = 1

	fun pattern(line1: String, line2: String, line3: String): ShapedRecipeSettings
	{
		patternLines.add(line1)
		patternLines.add(line2)
		patternLines.add(line3)
		return this
	}

	fun key(key: Char, item: Items): ShapedRecipeSettings
	{
		return key(key, item.toPath())
	}

	fun key(key: Char, blockItem: Blocks): ShapedRecipeSettings
	{
		return key(key, blockItem.toPath())
	}

	fun key(key: Char, itemPath: ResourcePath): ShapedRecipeSettings
	{
		// TODO: Tag???
		keys[key] = itemPath
		return this
	}

	fun result(count: Int): ShapedRecipeSettings
	{
		resultCount = count
		return this
	}

	fun result(itemPath: ResourcePath, count: Int = 1): ShapedRecipeSettings
	{
		result = itemPath
		resultCount = count
		return this
	}

	override fun serialisable(namespace: String, id: String): Recipe
	{
		return Recipe("$type", null,
			this.patternLines.toList(),
			this.keys.entries.associate { "${it.key}" to Recipe.Item(it.value.resolve(namespace, id)) },
			null,
			Recipe.Result(this.resultCount, result?.resolve(namespace, id) ?: "$namespace:$id")
		)
	}
}

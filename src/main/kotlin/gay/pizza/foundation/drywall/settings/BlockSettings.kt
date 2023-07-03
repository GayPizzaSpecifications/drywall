package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.Lang
import gay.pizza.foundation.drywall.enums.RecipeType

class BlockSettings(val id: String)
{
	private val _displayNames: MutableMap<Lang, String> = mutableMapOf()
	private var _tool: String? = null
	private var _level: String? = null

	private var _model: ModelSettings? = null
	private var _itemModel: ModelSettings? = null
	private var _blockState: BlockStateSettings? = null
	private var _lootTable: LootTableSettings? = null
	private var _recipes: MutableList<AbstractRecipeSettings> = mutableListOf()

	val displayNames: Map<Lang, String> get() = _displayNames.toMap()
	val tool: String? get() = _tool
	val level: String? get() = _level
	val model: ModelSettings? get() = _model
	val itemModel: ModelSettings? get() = _itemModel
	val blockState: BlockStateSettings? get() = _blockState
	val lootTable: LootTableSettings? get() = _lootTable
	val recipes: List<AbstractRecipeSettings> get() = _recipes.toList()

	fun displayName(text: String, lang: Lang = Lang.US_ENGLISH): BlockSettings
	{
		this._displayNames[lang] = text
		return this
	}

	fun needsTool(tool: String): BlockSettings
	{
		this._tool = tool
		return this
	}

	fun needsToolLevel(level: String): BlockSettings
	{
		this._level = level
		return this
	}

	fun model(settings: ModelSettings.() -> Unit): BlockSettings
	{
		return model(ModelSettings().apply(settings))
	}

	fun model(settings: ModelSettings): BlockSettings
	{
		this._model = settings
		return this
	}

	fun itemModel(settings: ModelSettings.() -> Unit): BlockSettings
	{
		return itemModel(ModelSettings().apply(settings))
	}

	fun itemModel(settings: ModelSettings): BlockSettings
	{
		this._itemModel = settings
		return this
	}

	fun blockState(settings: BlockStateSettings.() -> Unit): BlockSettings
	{
		return blockState(BlockStateSettings().apply(settings))
	}

	fun blockState(settings: BlockStateSettings): BlockSettings
	{
		this._blockState = settings
		return this
	}

	fun lootTable(settings: LootTableSettings.() -> Unit): BlockSettings
	{
		return lootTable(LootTableSettings().apply(settings))
	}

	fun lootTable(settings: LootTableSettings): BlockSettings
	{
		this._lootTable = settings
		return this
	}

	fun shapedRecipe(settings: ShapedRecipeSettings.() -> Unit): BlockSettings
	{
		return shapedRecipe(ShapedRecipeSettings().apply(settings))
	}

	fun shapedRecipe(settings: ShapedRecipeSettings): BlockSettings
	{
		this._recipes.add(settings)
		return this
	}

	fun shapelessRecipe(settings: ShapelessRecipeSettings.() -> Unit): BlockSettings
	{
		return shapelessRecipe(ShapelessRecipeSettings().apply(settings))
	}

	fun shapelessRecipe(settings: ShapelessRecipeSettings): BlockSettings
	{
		this._recipes.add(settings)
		return this
	}
}

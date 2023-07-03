package gay.pizza.foundation.drywall

import gay.pizza.foundation.drywall.enums.Lang
import gay.pizza.foundation.drywall.settings.*
import gay.pizza.foundation.drywall.util.ResourcePath
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class ResourceWriter(private val modId: String)
{
	@Serializable
	data class Tag(val replace: Boolean, val values: Collection<String>)

	private val langs: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
	private val mineables: MutableMap<String, MutableList<String>> = mutableMapOf()
	private val needsTool: MutableMap<String, MutableList<String>> = mutableMapOf()

	private val blockStates: MutableMap<String, BlockStateSettings.BlockState> = mutableMapOf()
	private val blockModels: MutableMap<String, ModelSettings.Model> = mutableMapOf()
	private val itemModels: MutableMap<String, ModelSettings.Model> = mutableMapOf()
	private val lootTables: MutableMap<String, LootTableSettings.LootTable> = mutableMapOf()
	private val recipes: MutableMap<String, AbstractRecipeSettings.Recipe> = mutableMapOf()

	private var defaultBlockState: BlockStateSettings? = null
	private var defaultBlockModel: ModelSettings? = null
	private var defaultItemModel: ModelSettings? = null
	private var defaultLootTable: LootTableSettings? = null

	private var fabricMod: FabricModSettings.FabricMod? = null
	private val mixins: MutableMap<String, MixinSettings.Mixins> = mutableMapOf()

	fun defaultItemModel(settings: ModelSettings.() -> Unit)
	{
		defaultItemModel(ModelSettings().apply(settings))
	}

	fun defaultItemModel(settings: ModelSettings)
	{
		this.defaultItemModel = settings
	}

	fun defaultLootTable(settings: LootTableSettings.() -> Unit)
	{
		defaultLootTable(LootTableSettings().apply(settings))
	}

	fun defaultLootTable(settings: LootTableSettings)
	{
		this.defaultLootTable = settings
	}

	fun defaultBlockState(settings: BlockStateSettings.() -> Unit)
	{
		defaultBlockState(BlockStateSettings().apply(settings))
	}

	fun defaultBlockState(settings: BlockStateSettings)
	{
		this.defaultBlockState = settings
	}

	fun text(key: String, string: String, lang: Lang = Lang.US_ENGLISH)
	{
		langs.getOrPut(lang.toString()) { mutableMapOf() }[key] = string
	}

	fun itemGroupName(name: String, lang: Lang = Lang.US_ENGLISH)
	{
		text("itemGroup.$modId.$modId", name, lang)
	}

	fun block(id: String, settings: BlockSettings.() -> Unit)
	{
		block(BlockSettings(id).apply(settings))
	}

	fun block(settings: BlockSettings)
	{
		val blockId = settings.id
		val jsonName = "${blockId}.json"
		val resPath = ResourcePath.PATH_BLOCK.resolve(modId, blockId)

		settings.displayNames.forEach { text("block.$modId.${blockId}", it.value, it.key) }
		settings.tool?.let { mineables.getOrPut(it) { mutableListOf() }.add(resPath) }
		settings.level?.let { needsTool.getOrPut("needs_${it}_tool") { mutableListOf() }.add(resPath) }

		(settings.blockState ?: defaultBlockState)?.let { blockStates[jsonName] = it.serialisable(modId, blockId) }
		(settings.model ?: defaultBlockModel)?.let { blockModels[jsonName] = it.serialisable(modId, blockId) }
		(settings.itemModel ?: defaultItemModel)?.let { itemModels[jsonName] = it.serialisable(modId, blockId) }
		(settings.lootTable ?: defaultLootTable)?.let { lootTables[jsonName] = it.serialisable(modId, blockId) }
		settings.recipes.forEachIndexed { idx, it ->
			val recipeName = if (idx > 0)
				"${blockId}_${(idx).toString(16)}.json"
			else jsonName
			recipes[recipeName] = it.serialisable(modId, blockId)
		}
	}

	fun fabricMod(settings: FabricModSettings.() -> Unit)
	{
		fabricMod(FabricModSettings().apply(settings))
	}

	fun fabricMod(settings: FabricModSettings)
	{
		fabricMod = settings.serialisable(modId)
		settings.mixins.forEach {
			if (it.value.packagePath != null)
				mixins.put(it.key ?: "$modId.mixins.json", it.value.serialisable())
		}
	}

	fun writeAll(dirResources: Path)
	{
		val dirAssets      = dirResources.resolve(Path("assets", modId))
		val dirBlockStates = dirAssets.resolve("blockstates")
		val dirLang        = dirAssets.resolve("lang")
		val dirBlockModel  = dirAssets.resolve(Path("models", "block"))
		val dirItemModel   = dirAssets.resolve(Path("models", "item"))
		var dirModData     = dirResources.resolve(Path("data", modId))
		val dirLootBlocks  = dirModData.resolve(Path("loot_tables", "blocks"))
		var dirRecipes     = dirModData.resolve(Path("recipes"))
		val dirTagBlocks   = dirResources.resolve(Path("data", "minecraft", "tags", "blocks"))
		val dirMcMineable  = dirTagBlocks.resolve("mineable")

		blockStates.forEach { encodeWrite(it.value, dirBlockStates.resolve(it.key)) }
		blockModels.forEach { encodeWrite(it.value, dirBlockModel.resolve(it.key)) }
		itemModels.forEach { encodeWrite(it.value, dirItemModel.resolve(it.key)) }
		lootTables.forEach { encodeWrite(it.value, dirLootBlocks.resolve(it.key)) }
		recipes.forEach { encodeWrite(it.value, dirRecipes.resolve(it.key)) }

		langs.forEach { encodeWrite(it.value, dirLang.resolve("${it.key}.json")) }
		mineables.forEach { encodeWrite(Tag(false, it.value), dirMcMineable.resolve("${it.key}.json")) }
		needsTool.forEach { encodeWrite(Tag(false, it.value), dirTagBlocks.resolve("${it.key}.json")) }

		fabricMod.let { encodeWrite(it, dirResources.resolve("fabric.mod.json")) }
		mixins.forEach { encodeWrite(it.value, dirResources.resolve(it.key)) }
	}

	@OptIn(ExperimentalSerializationApi::class)
	private val json = Json {
		encodeDefaults = true
		explicitNulls = false
		prettyPrint = true
		prettyPrintIndent = "  "
	}

	private fun openFile(filePath: Path): FileOutputStream
	{
		val dirPath = filePath.parent
		if (!dirPath.toFile().isDirectory)
			Files.createDirectories(dirPath)
		return FileOutputStream(filePath.toFile())
	}

	@OptIn(ExperimentalSerializationApi::class)
	@SuppressWarnings("deprecation")
	private inline fun <reified T> encodeWrite(obj: T, fileName: Path)
	{
		val out = openFile(fileName)
		json.encodeToStream(obj, out)
		out.write('\n'.code)
		out.flush()
	}
}

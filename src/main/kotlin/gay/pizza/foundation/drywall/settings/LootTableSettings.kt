package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.util.ResourcePath
import gay.pizza.foundation.drywall.enums.ConditionType
import gay.pizza.foundation.drywall.enums.LootContextType
import gay.pizza.foundation.drywall.enums.LootEntryType
import kotlinx.serialization.Serializable

class LootTableSettings
{
	data class Pool(val rolls: Int, val entries: List<Entry>, val conditions: List<Predicate>)
	{
		data class Entry(val type: LootEntryType, val name: ResourcePath)
		data class Predicate(val condition: ConditionType)
	}

	companion object
	{
		val DEFAULT_BLOCK = LootTableSettings()
			.type(LootContextType.BLOCK)
			.pool(1, listOf(
				Pool.Entry(LootEntryType.ITEM, ResourcePath.PATH_BLOCK)
			))
	}

	private var ctxType: LootContextType? = null
	private val pools: MutableList<Pool> = mutableListOf()

	fun type(type: LootContextType): LootTableSettings
	{
		this.ctxType = type
		return this
	}

	fun pool(rolls: Int, entries: List<Pool.Entry>, conditions: List<Pool.Predicate> = emptyList()): LootTableSettings
	{
		this.pools.add(Pool(rolls, entries, conditions))
		return this
	}

	@Serializable
	data class LootTable(val type: String?, val pools: List<Pool>)
	{
		@Serializable
		data class Pool(val rolls: Int, val entries: List<Entry>, val conditions: List<Predicate>? = null)
		{
			@Serializable
			data class Predicate(val condition: String)
			@Serializable
			data class Entry(val type: String, val name: String)
		}
	}

	fun serialisable(namespace: String, blockId: String): LootTable
	{
		// TODO: conditions
		return LootTable(this.ctxType?.toString(), this.pools.map {
			LootTable.Pool(
				it.rolls,
				it.entries.map {
					LootTable.Pool.Entry(it.type.toString(), it.name.resolve(namespace, blockId))
				},
				if (it.conditions.isNotEmpty()) it.conditions.map {
					LootTable.Pool.Predicate(it.condition.toString())
				} else null
			)
		})
	}
}

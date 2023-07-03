package gay.pizza.foundation.drywall.enums

enum class LootEntryType(val value: String)
{
	ITEM("item"),
	TAG("tag"),
	LOOT_TABLE("loot_table"),
	GROUP("group"),
	ALTERNATIVES("alternatives"),
	SEQUENCE("sequence"),
	DYNAMIC("dynamic"),
	EMPTY("empty");

	override fun toString(): String
	{
		return "minecraft:$value"
	}
}

package gay.pizza.foundation.drywall.enums

enum class RecipeType(val value: String, val special: String? = null)
{
	BLASTING("blasting"),
	COOKING("campfire_cooking"),
	SHAPED("crafting_shaped"),
	SHAPELESS("crafting_shapeless"),
	SMELTING("smelting"),
	SMITHING("smithing"),
	SMOKING("smoking"),
	STONECUTTING("stonecutting"),

	SPECIAL_ARMORDYE("crafting_special_", "armordye"),
	SPECIAL_BANNERDUPLICATE("crafting_special_", "bannerduplicate"),
	SPECIAL_BOOKCLONING("crafting_special_", "bookcloning"),
	SPECIAL_FIREWORK_ROCKET("crafting_special_", "firework_rocket"),
	SPECIAL_FIREWORK_STAR("crafting_special_", "firework_star"),
	SPECIAL_FIREWORK_STAR_FADE("crafting_special_", "firework_star_fade"),
	SPECIAL_MAPCLONING("crafting_special_", "mapcloning"),
	SPECIAL_MAPEXTENDING("crafting_special_", "mapextending"),
	SPECIAL_REPAIRITEM("crafting_special_", "repairitem"),
	SPECIAL_SHIELDDECORATION("crafting_special_", "shielddecoration"),
	SPECIAL_SHULKERBOXCOLORING("crafting_special_", "shulkerboxcoloring"),
	SPECIAL_TIPPEDARROW("crafting_special_", "tippedarrow"),
	SPECIAL_SUSPICIOUSSTEW("crafting_special_", "suspiciousstew");

	override fun toString(): String
	{
		return special?.let { "minecraft:$value$special" } ?: "minecraft:$value"
	}
}

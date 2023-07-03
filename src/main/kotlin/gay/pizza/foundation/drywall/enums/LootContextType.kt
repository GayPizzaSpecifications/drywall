package gay.pizza.foundation.drywall.enums

enum class LootContextType(val value: String)
{
	EMPTY("empty"),
	CHEST("chest"),
	COMMAND("command"),
	SELECTOR("selector"),
	FISHING("fishing"),
	ENTITY("entity"),
	GIFT("gift"),
	BARTER("barter"),
	ADVANCEMENT_REWARD("advancement_reward"),
	ADVANCEMENT_ENTITY("advancement_entity"),
	GENERIC("generic"),
	BLOCK("block");

	override fun toString(): String
	{
		return "minecraft:$value"
	}
}

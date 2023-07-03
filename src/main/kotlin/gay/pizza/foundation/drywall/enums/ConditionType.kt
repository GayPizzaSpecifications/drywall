package gay.pizza.foundation.drywall.enums

enum class ConditionType(private val value: String)
{
	SURVIVES_EXPLOSION("survives_explosion");

	override fun toString(): String
	{
		return "minecraft:$value"
	}
}

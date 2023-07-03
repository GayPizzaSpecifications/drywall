package gay.pizza.foundation.drywall.enums

public enum class Lang(val value: String)
{
	US_ENGLISH("en_us"),
	FRENCH("fr_fr");

	override fun toString(): String
	{
		return value
	}
}

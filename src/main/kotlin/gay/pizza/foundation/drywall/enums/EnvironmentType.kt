package gay.pizza.foundation.drywall.enums

enum class EnvironmentType(private val value: String)
{
	EVERYWHERE("*"),
	CLIENT("client"),
	SERVER("server");

	override fun toString(): String
	{
		return value
	}
}

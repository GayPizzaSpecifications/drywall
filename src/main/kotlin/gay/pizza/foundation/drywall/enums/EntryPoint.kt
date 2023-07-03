package gay.pizza.foundation.drywall.enums

data class EntryPoint(val name: String)
{
	companion object
	{
		final val MAIN = EntryPoint("main")
		final val CLIENT = EntryPoint("client")
		final val SERVER = EntryPoint("server")
	}
}

package gay.pizza.foundation.drywall.util

class ResourcePath private constructor(private val namespace: String?, vararg path: String)
{
	companion object
	{
		val ID = "*"

		val PATH_BLOCK = ResourcePath.of(ID)
		val PATH_BLOCK_MODEL = ResourcePath.of("block", ID)

		fun of(vararg path: String): ResourcePath
		{
			return ResourcePath(null, *path)
		}

		fun ofNamespace(namespace: String, vararg path: String): ResourcePath
		{
			return ResourcePath(namespace, *path)
		}
	}

	private val tokens: List<String>

	init
	{
		this.tokens = path.asList()
	}

	fun resolve(namespace: String, id: String): String
	{
		return "${this.namespace ?: namespace}:${
			tokens.joinToString(separator = "/") {
				when (it)
				{
					ID -> id
					else -> it
				}
			}}"
	}

	override fun toString(): String
	{
		val path = tokens.joinToString(separator = "/")
		return namespace?.let { "$it:$path" } ?: path
	}
}

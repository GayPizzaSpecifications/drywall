package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.util.ResourcePath
import kotlinx.serialization.Serializable

class ModelSettings
{
	companion object
	{
		val DEFAULT_ITEM_BLOCK_MODEL = ModelSettings().parent(ResourcePath.PATH_BLOCK_MODEL)
	}

	private var parent: ResourcePath? = null

	fun parent(path: ResourcePath): ModelSettings
	{
		this.parent = path
		return this
	}

	@Serializable
	data class Model(val parent: String?, val textures: Map<String, String>? = null)

	fun serialisable(namespace: String, blockId: String): Model
	{
		//TODO: textures
		return Model(this.parent?.resolve(namespace, blockId))
	}
}

package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.util.ResourcePath
import kotlinx.serialization.Serializable

class BlockStateSettings
{
	private data class Variant(
		val model: ResourcePath,
		val x: Int,
		val y: Int,
		val uvlock: Boolean)

	companion object
	{
		const val STATE_NORMAL = ""

		val DEFAULT_BLOCK_MODEL = BlockStateSettings()
			.variant(model = ResourcePath.PATH_BLOCK_MODEL)
		val DEFAULT_HORIZONTAL_FACING_VARIANTS = BlockStateSettings()
			.horizontalFacingVariants()
		val DEFAULT_AXIS_VARIANTS = BlockStateSettings()
			.axisVariants()
		val DEFAULT_HORIZONTAL_AXIS_VARIANTS = BlockStateSettings()
			.horizontalAxisVariants()
	}

	private val variants: MutableMap<String, Variant> = mutableMapOf()

	fun variant(state: String = STATE_NORMAL,
		model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL,
		x: Int = 0,
		y: Int = 0,
		uvlock: Boolean = false): BlockStateSettings
	{
		variants[state] = Variant(model, x, y, uvlock)
		return this
	}

	fun horizontalFacingVariants(extra: String, model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL, yNorth: Int = 0): BlockStateSettings
	{
		variant("${extra},facing=north", model, y = Math.floorMod(yNorth, 360))
		variant("${extra},facing=east",  model, y = Math.floorMod(yNorth +  90, 360))
		variant("${extra},facing=south", model, y = Math.floorMod(yNorth + 180, 360))
		variant("${extra},facing=west",  model, y = Math.floorMod(yNorth + 270, 360))
		return this
	}

	fun horizontalFacingVariants(model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL, yNorth: Int = 0): BlockStateSettings
	{
		variant("facing=north", model, y = Math.floorMod(yNorth, 360))
		variant("facing=east",  model, y = Math.floorMod(yNorth +  90, 360))
		variant("facing=south", model, y = Math.floorMod(yNorth + 180, 360))
		variant("facing=west",  model, y = Math.floorMod(yNorth + 270, 360))
		return this
	}

	fun axisVariants(extra: String, model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL): BlockStateSettings
	{
		variant("${extra},axis=y", model)
		variant("${extra},axis=z", model, x = 270)
		variant("${extra},axis=x", model, x = 90, y = 90)
		return this
	}

	fun axisVariants(model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL): BlockStateSettings
	{
		variant("axis=y", model)
		variant("axis=z", model, x = 270)
		variant("axis=x", model, x = 90, y = 90)
		return this
	}

	fun horizontalAxisVariants(model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL, x: Int = 90, z: Int = 0): BlockStateSettings
	{
		variant("axis=x", model, y = Math.floorMod(x, 360))
		variant("axis=z", model, y = Math.floorMod(z, 360))
		return this
	}

	fun horizontalAxisVariants(extra: String, model: ResourcePath = ResourcePath.PATH_BLOCK_MODEL, x: Int = 90, z: Int = 0): BlockStateSettings
	{
		variant("${extra},axis=x", model, y = Math.floorMod(x, 360))
		variant("${extra},axis=z", model, y = Math.floorMod(z, 360))
		return this
	}

	@Serializable
	data class BlockState(val variants: Map<String, Variant>)
	{
		@Serializable
		data class Variant(val model: String, val x: Int? = null, val y: Int? = null, val uvlock: Boolean? = null)
	}

	fun serialisable(namespace: String, blockId: String): BlockState
	{
		return BlockState(this.variants.entries.associate {
			it.key to BlockState.Variant(
				it.value.model.resolve(namespace, blockId),
				if (it.value.x != 0) it.value.x else null,
				if (it.value.y != 0) it.value.y else null,
				if (it.value.uvlock) true else null)
		})
	}
}

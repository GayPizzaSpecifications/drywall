package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.MixinCompatibilityLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MixinSettings()
{
	private var _packagePath: String? = null
	private var _compatibilityLevel: MixinCompatibilityLevel? = null
	private val _mixins: MutableList<String> = mutableListOf()
	private val _client: MutableList<String> = mutableListOf()
	private val _server: MutableList<String> = mutableListOf()
	//private val _injectors: MutableMap<String, Int> = mutableMapOf()

	val packagePath: String? get() = _packagePath

	fun packagePath(path: String): MixinSettings
	{
		_packagePath = path
		return this
	}

	fun compatibilityLevel(javaVersion: MixinCompatibilityLevel): MixinSettings
	{
		_compatibilityLevel = javaVersion
		return this
	}

	fun mixin(className: String): MixinSettings
	{
		_mixins.add(className)
		return this
	}

	fun clientMixin(className: String): MixinSettings
	{
		_client.add(className)
		return this
	}

	fun serverMixin(className: String): MixinSettings
	{
		_server.add(className)
		return this
	}

	//fun injector(className: String, what: Int): MixinSettings
	//{
	//	_injectors.put(className, what)
	//	return this
	//}

	@Serializable
	data class Mixins(
		val required: Boolean,
		val minVersion: String,
		@SerialName("package") val packagePath: String,
		val compatibilityLevel: String?,
		val mixins: List<String>?,
		val client: List<String>?,
		val server: List<String>?)
		//val injectors: Map<String, Int>?)

	//TODO: injector settings
	//defaultRequire: int
	//defaultGroup: str
	//namespace: str
	//injectionPoints: list<str>
	//dynamicSelectors: list<str>
	//maxShiftBy: int

	fun serialisable(): Mixins
	{
		return Mixins(
			true,
			"0.8",
			_packagePath!!,
			_compatibilityLevel?.let { "$it" },
			_mixins.ifEmpty { null },
			_client.ifEmpty { null },
			_server.ifEmpty { null })
			//_injectors.ifEmpty { null })
	}
}

package gay.pizza.foundation.drywall.settings

import gay.pizza.foundation.drywall.enums.EntryPoint
import gay.pizza.foundation.drywall.enums.EnvironmentType
import kotlinx.serialization.Serializable
import org.gradle.api.internal.provider.MissingValueException

class FabricModSettings
{
	private var _modId: String? = null
	private var _version: String? = null

	private var _name: String? = null
	private var _description: String? = null
	private var _license: String? = null
	private var _icon: String? = null
	private var _environment: EnvironmentType = EnvironmentType.EVERYWHERE

	private val _authors: MutableList<String> = mutableListOf()
	private val _entrypoints: MutableMap<EntryPoint, MutableList<String>> = mutableMapOf()
	private val _depends: MutableMap<String, String> = mutableMapOf()

	private val _mixins: MutableMap<String?, MixinSettings> = mutableMapOf()

	val mixins: Map<String?, MixinSettings> get() = _mixins.toMap()

	fun id(id: String): FabricModSettings
	{
		_modId = id
		return this
	}

	fun version(version: Any): FabricModSettings
	{
		_version = "${version}"
		return this
	}

	fun displayName(name: String): FabricModSettings
	{
		_name = name
		return this
	}

	fun description(text: String): FabricModSettings
	{
		_description = text
		return this
	}

	fun author(name: String): FabricModSettings
	{
		_authors.add(name)
		return this
	}

	fun license(name: String): FabricModSettings
	{
		_license = name
		return this
	}

	fun icon(path: String): FabricModSettings
	{
		_icon = path
		return this
	}

	fun environment(environment: EnvironmentType): FabricModSettings
	{
		_environment = environment
		return this
	}

	fun entryPoint(entryPoint: EntryPoint, vararg classPath: String): FabricModSettings
	{
		_entrypoints.getOrPut(entryPoint) { mutableListOf() } += classPath
		return this
	}

	fun depends(modName: String, modVersion: String = "*"): FabricModSettings
	{
		_depends[modName] = modVersion
		return this
	}

	fun mixins(settings: MixinSettings.() -> Unit): FabricModSettings
	{
		return mixins(MixinSettings().apply(settings))
	}

	fun mixins(settings: MixinSettings): FabricModSettings
	{
		_mixins.put(null, settings)
		return this
	}

	fun mixins(name: String): FabricModSettings
	{
		return mixins(name, MixinSettings())
	}

	fun mixins(name: String, settings: MixinSettings.() -> Unit): FabricModSettings
	{
		return mixins(name, MixinSettings().apply(settings))
	}

	fun mixins(name: String, settings: MixinSettings): FabricModSettings
	{
		_mixins.put(name, settings)
		return this
	}

	@Serializable
	data class FabricMod(
		val schemaVersion: Int,
		val id: String,
		val version: String,
		val name: String?,
		val description: String?,
		val authors: List<String>?,
		val license: String?,
		val icon: String?,
		val environment: String?,
		val entrypoints: Map<String, List<String>>?,
		val mixins: List<String>?,
		val depends: Map<String, String>?)

	fun serialisable(namespace: String): FabricMod
	{
		if (_version.isNullOrBlank())
			throw MissingValueException("missing required version string")

		return FabricMod(1,
			_modId ?: namespace,
			_version!!,
			_name,
			_description,
			_authors,
			_license,
			_icon ?: "assets/$namespace/icon.png",
			"$_environment",
			_entrypoints.entries.associate { it.key.name to it.value.toList() },
			mixins.ifEmpty { null }?.let{it.keys.map { it ?: "$namespace.mixins.json" } },
			_depends)
	}
}

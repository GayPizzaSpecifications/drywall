package gay.pizza.foundation.drywall

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ResourcesGenerator : DefaultTask()
{
	@get:Input
	var modId: String = ""
	@get:Input
	var configuration: ResourceWriter.() -> Unit = {}
	@get:InputDirectory
	var outputDirectory: File = File("")

	@TaskAction
	fun generateResources()
	{
		val writer = ResourceWriter(modId).apply(configuration)
		writer.writeAll(outputDirectory.toPath())
	}
}

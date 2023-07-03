package gay.pizza.foundation.drywall

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ResourcesGeneratorPlugin: Plugin<Project>
{
	override fun apply(target: Project)
	{
		val genResources = target.tasks.create<ResourcesGenerator>("genResources")
		target.afterEvaluate {
			project.tasks.getByName("jar").dependsOn(genResources)
		}
	}
}

package com.autonomousapps.fixtures

import java.io.File

class JavaAppWithLibraryProject(
  rootSpec: RootSpec = RootSpec(),
  librarySpecs: List<LibrarySpec>? = null
) : ProjectDirProvider {

  private val rootProject = RootProject(rootSpec)

  override val projectDir: File
    get() = TODO("Not yet implemented")

  // A collection of library modules, keyed by their respective names.
  private val modules: Map<String, Module> = mapOf(
    *librarySpecs?.map { spec ->
      spec.name to libraryFactory(projectDir, spec)
    }?.toTypedArray() ?: emptyArray()
  )

  override fun project(moduleName: String) = modules[moduleName] ?: error("No '$moduleName' project found!")
}

package com.autonomousapps.jvm

import com.autonomousapps.AbstractFunctionalSpec
import com.autonomousapps.fixtures.MultiModuleJavaLibraryProject
import com.autonomousapps.fixtures.ProjectDirProvider
import com.autonomousapps.fixtures.RootSpec
import spock.lang.Unroll

import static com.autonomousapps.fixtures.JvmFixtures.getCONSUMER_CONSTANT_JAVA
import static com.autonomousapps.fixtures.JvmFixtures.getCONSUMER_CONSTANT_JAVA
import static com.autonomousapps.fixtures.JvmFixtures.getPRODUCER_CONSTANT_JAVA
import static com.autonomousapps.utils.Runner.build

final class CompileSpec extends AbstractFunctionalSpec {

  private ProjectDirProvider javaLibraryProject = null

  def cleanup() {
    if (javaLibraryProject != null) {
      clean(javaLibraryProject)
    }
  }

  @Unroll
  def "recognizes compile configuration (#gradleVersion)"() {
    given:
    def libSpecs = [CONSUMER_CONSTANT_JAVA, PRODUCER_CONSTANT_JAVA]
    javaLibraryProject = new MultiModuleJavaLibraryProject(RootSpec.defaultRootSpec(libSpecs), libSpecs)

    when:
    build(gradleVersion, javaLibraryProject, 'buildHealth')

    then:
    def actualUnusedDependencies = javaLibraryProject.unusedDependenciesFor(CONSUMER_CONSTANT_JAVA)
    [] == actualUnusedDependencies

    where:
    gradleVersion << gradleVersions()
  }
}

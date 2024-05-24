package org.nixos.gradle2nix.util

import java.lang.reflect.Method
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.artifacts.ivyservice.ArtifactCachesProvider
import org.gradle.api.internal.artifacts.ivyservice.modulecache.FileStoreAndIndexProvider
import org.gradle.api.invocation.Gradle
import org.gradle.internal.hash.ChecksumService
import org.gradle.internal.operations.BuildOperationAncestryTracker
import org.gradle.internal.operations.BuildOperationListenerManager

internal inline val Gradle.artifactCachesProvider: ArtifactCachesProvider
    get() = service()

internal inline val Gradle.buildOperationListenerManager: BuildOperationListenerManager
    get() = service()

internal inline val Gradle.checksumService: ChecksumService
    get() = service()

internal inline val Gradle.fileStoreAndIndexProvider: FileStoreAndIndexProvider
    get() = service()

internal inline fun <reified T> Gradle.service(): T =
    (this as GradleInternal).services.get(T::class.java)

private val canSafelyBeResolvedMethod: Method? = try {
    val dc = Class.forName("org.gradle.internal.deprecation.DeprecatableConfiguration")
    dc.getMethod("canSafelyBeResolved")
} catch (e: ReflectiveOperationException) {
    null
}

internal fun Configuration.canSafelyBeResolved(): Boolean =
    canSafelyBeResolvedMethod?.invoke(this) as? Boolean ?: isCanBeResolved

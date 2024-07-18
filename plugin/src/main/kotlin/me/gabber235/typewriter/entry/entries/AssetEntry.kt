package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Generated
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.StaticEntry
import me.gabber235.typewriter.utils.failure
import org.koin.java.KoinJavaComponent

@Tags("asset")
interface AssetEntry : StaticEntry {
    @Help("The path to the asset.")
    val path: String
}

suspend fun AssetEntry.data(): String? {
    return KoinJavaComponent.get<AssetManager>(AssetManager::class.java).fetchAsset(this)
}

suspend fun AssetEntry.data(data: String) {
    KoinJavaComponent.get<AssetManager>(AssetManager::class.java).storeAsset(this, data)
}

/**
 * Artifacts are assets that are generated by the plugin itself.
 */
@Tags("artifact")
interface ArtifactEntry : AssetEntry {
    @Help("A unique identifier for the artifact. SHOULD NOT BE CHANGED!")
    @Generated
    val artifactId: String

    val extension: String
        get() = "json"

    override val path: String
        get() = "artifacts/${artifactId}.${extension}"
}

fun getAssetFromFieldValue(fieldValue: Any?): Result<AssetEntry> {
    if (fieldValue !is String) {
        return failure("Field value must be a string!")
    }

    if (fieldValue.isBlank()) {
        return failure("A asset must be selected.")
    }

    val artifact = Query.findById<AssetEntry>(fieldValue)
        ?: return failure("Could not find artifact with id '$fieldValue'")

    return Result.success(artifact)
}
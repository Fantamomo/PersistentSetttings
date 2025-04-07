package com.fantamomo.persistent.settings

import com.fantamomo.persistent.container.BufferedPersistentDataContainer
import com.fantamomo.persistent.container.MutablePersistentDataContainer
import com.fantamomo.persistent.container.toBuffered
import com.fantamomo.persistent.serializer.SerializerStrategy
import java.io.InputStream
import java.io.OutputStream

/**
 * Abstract class for settings management.
 *
 * For the implemented class, it is recommended to be an object.
 *
 * Wich this class, it is simpler to define settings entries with the [create] methode.
 *
 * @param namespace The namespace of this settings, it is only recommended to set,
 * when the [BufferedPersistentDataContainer] is direct use with [com.fantamomo.persistent.container.copyTo].
 * @param input A [InputStream] where the settings should load of, it is optional to set, the [loadFrom] methode does the same.
 * @param serializer A [SerializerStrategy] that defines how the data should load from [input]
 *
 * @author Fantamomo
 * @since 1.0-SNAPSHOT
 */
abstract class Settings(
    namespace: String = "",
    input: InputStream? = null,
    serializer: SerializerStrategy = SerializerStrategy,
) : ParentSettingsNode() {

    /**
     * The [BufferedPersistentDataContainer] which contains all entries.
     */
    @JvmField
    internal val container = MutablePersistentDataContainer().toBuffered()

    init {
        input?.let { container.loadFrom(it, serializer) }
    }

    /**
     * The path which is used in [SettingsNode]
     */
    final override val path: String = namespace

    /**
     * Override the [settings] property
     */
    final override val settings: Settings = this

    /**
     * Delegates the call to [com.fantamomo.persistent.container.PersistentDataContainer.saveTo]
     */
    protected fun saveTo(output: OutputStream, serializer: SerializerStrategy = SerializerStrategy) {
        container.saveTo(output, serializer)
    }

    /**
     * Delegates the call to [MutablePersistentDataContainer.loadFrom]
     */
    protected fun loadFrom(input: InputStream, serializer: SerializerStrategy = SerializerStrategy) {
        container.loadFrom(input, serializer)
    }

    /**
     * Returns the [container] to the implemented class
     */
    protected fun getContainer(): BufferedPersistentDataContainer = container
}
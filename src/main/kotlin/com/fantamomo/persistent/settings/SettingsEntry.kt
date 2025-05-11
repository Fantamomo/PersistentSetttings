package com.fantamomo.persistent.settings

import com.fantamomo.persistent.container.getOrDefault
import com.fantamomo.persistent.type.PersistentDataType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Class that holds the data of an [key].
 *
 * @param key The key of the entry
 * @param type The type of the value
 * @param node The [ParentSettingsNode] where the entry was created (It is only used for the [path] and [ParentSettingsNode.settings]
 * @param default The lazy default initializer
 */
class SettingsEntry<P : Any, C : Any> internal constructor(
    internal val node: ParentSettingsNode,
    val type: PersistentDataType<P, C>,
    key: String? = null,
    internal val default: () -> C,
) : ReadWriteProperty<ParentSettingsNode, C> {

    var key: String? = key
        internal set

    /**
     * The path where when saved the value should be saved and loaded
     */
    val path: String by lazy {
        node.path + ":" + (key
            ?: throw IllegalArgumentException("No key was provided, provide a key or call getValue or setValue first"))
    }

    /**
     * Methode that return the [value] in the `by`-Syntax
     *
     * When the [key] is not set, it will be computed by [KProperty.name]
     */
    override fun getValue(
        thisRef: ParentSettingsNode,
        property: KProperty<*>,
    ): C {
        if (key == null) key = property.name
        return value
    }

    /**
     * Property for getting and settings the value,
     * in [Settings] over [com.fantamomo.persistent.container.BufferedPersistentDataContainer]
     */
    var value: C
        get() = node.settings.container.getOrDefault(path, type, default)
        set(value) = node.settings.container.set(path, type, value)

    /**
     * Methode that sets the [value] in the `by`-Syntax
     *
     * When the [key] is not set, it will be computed by [KProperty.name]
     */
    override fun setValue(
        thisRef: ParentSettingsNode,
        property: KProperty<*>,
        value: C,
    ) {
        if (key == null) key = property.name
        this.value = value
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun set(value: C) {
        this.value = value
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun get() = value
}
package com.fantamomo.persistent.settings

import com.fantamomo.persistent.type.PersistentDataType

/**
 * Sealed class for [Settings] and [SettingsNode].
 *
 * It declared function for creating [SettingsEntry], for simple settings management.
 *
 * @author Fantamomo
 * @since 1.0-SNAPSHOT
 */
sealed class ParentSettingsNode {
    /**
     * [Settings] instance of this hierarchy.
     *
     * It is overridden by subclasses.
     */
    internal abstract val settings: Settings

    /**
     * Property that holds the path from the hierarchy.
     *
     * It is overridden by the subclasses to complete the hierarchy.
     *
     * @throws IllegalStateException when it is implemented by a class which has no [kotlin.reflect.KClass.simpleName]
     */
    open val path: String by lazy {
        this::class.simpleName
            ?: throw IllegalStateException("Implementation of ParentSettingsNode must not be anonymous")
    }

    /**
     * A node in the Settings hierarchy.
     *
     * It implements [ParentSettingsNode] to allow a nested settings hierarchy.
     */
    abstract inner class SettingsNode : ParentSettingsNode() {
        /**
         * Override [settings] to complete the hierarchy.
         */
        final override val settings: Settings = this@ParentSettingsNode.settings

        /**
         * Override [path] to complete the hierarchy.
         */
        final override val path: String by lazy {
            "/" + this@ParentSettingsNode.path + (this::class.simpleName
                ?: throw IllegalStateException("Implementation of ParentSettingsNode must not be anonymous"))

        }
    }

    /**
     * Creates a new [SettingsEntry] with the [key], [type] and [default].
     *
     * @param key The key of the entry
     * @param type The type of the value.
     * @param default The default value when it not exist in the [Settings]
     */
    protected fun <P : Any, C : Any> create(key: String, type: PersistentDataType<P, C>, default: C) =
        SettingsEntry(this, type, key) { default }


    /**
     * Creates a new [SettingsEntry] with the [key], [type] and [default].
     *
     * @param key The key of the entry
     * @param type The type of the value.
     * @param default The lazy default value when it not exist in the [Settings]
     */
    protected fun <P : Any, C : Any> create(key: String, type: PersistentDataType<P, C>, default: () -> C) =
        SettingsEntry(this, type, key, default)

    /**
     * Creates a new [SettingsEntry] with the [type] and [default] value.
     *
     * **Note**: When not present a key, it will later initialize which the [kotlin.reflect.KProperty.name]
     *
     * @param type The type of the value
     * @param default The default value
     */
    protected fun <P : Any, C : Any> create(type: PersistentDataType<P, C>, default: C) =
        SettingsEntry(this, type) { default }

    /**
     * Creates a new [SettingsEntry] with the [type] and [default] value.
     *
     * **Note**: When not present a key, it will later initialize which the [kotlin.reflect.KProperty.name]
     *
     * @param type The type of the value
     * @param default The lazy default value
     */
    protected fun <P : Any, C : Any> create(type: PersistentDataType<P, C>, default: () -> C) =
        SettingsEntry(this, type, null, default)


}
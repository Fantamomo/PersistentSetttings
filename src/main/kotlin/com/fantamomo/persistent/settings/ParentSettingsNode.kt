package com.fantamomo.persistent.settings

import com.fantamomo.persistent.type.PersistentDataType

sealed class ParentSettingsNode {
    internal abstract val settings: Settings

    internal open val path: String by lazy {
        this::class.simpleName
            ?: throw IllegalStateException("Implementation of ParentSettingsNode can not be anonymise")
    }

    abstract inner class SettingsNode : ParentSettingsNode() {
        final override val settings: Settings = this@ParentSettingsNode.settings
        final override val path: String by lazy {
            "/" + this@ParentSettingsNode.path + (this::class.simpleName
                ?: throw IllegalStateException("Implementation of SettingsNode can not be anonymise"))

        }
    }

    fun <P : Any, C : Any> create(key: String, type: PersistentDataType<P, C>, default: C) =
        SettingsEntry(this, type, key) { default }

    fun <P : Any, C : Any> create(key: String, type: PersistentDataType<P, C>, default: () -> C) =
        SettingsEntry(this, type, key, default)

    fun <P : Any, C : Any> create(type: PersistentDataType<P, C>, default: C) =
        SettingsEntry(this, type) { default }

    fun <P : Any, C : Any> create(type: PersistentDataType<P, C>, default: () -> C) =
        SettingsEntry(this, type, null, default)


}
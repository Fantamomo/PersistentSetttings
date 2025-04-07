package com.fantamomo.persistent.settings

import com.fantamomo.persistent.container.getOrDefault
import com.fantamomo.persistent.type.PersistentDataType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SettingsEntry<P : Any, C : Any> internal constructor(
    internal val node: ParentSettingsNode,
    internal val type: PersistentDataType<P, C>,
    internal var key: String? = null,
    internal val default: () -> C,
) : ReadWriteProperty<ParentSettingsNode, C> {

    val path: String by lazy {
        node.path + ":" + (key
            ?: throw IllegalArgumentException("No key was provided, provide a key or call getValue or setValue first"))
    }

    override fun getValue(
        thisRef: ParentSettingsNode,
        property: KProperty<*>,
    ): C {
        if (key == null) key = property.name
        return value
    }

    var value: C
        get() = node.settings.container.getOrDefault(path, type, default)
        set(value) = node.settings.container.set(path, type, value)

    override fun setValue(
        thisRef: ParentSettingsNode,
        property: KProperty<*>,
        value: C,
    ) {
        if (key == null) key = property.name
        this.value = value
    }

}
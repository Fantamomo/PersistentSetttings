package com.fantamomo.persistent.settings

import com.fantamomo.persistent.container.BufferedPersistentDataContainer
import com.fantamomo.persistent.container.MutablePersistentDataContainer
import com.fantamomo.persistent.container.toBuffered
import com.fantamomo.persistent.serializer.SerializerStrategy
import java.io.InputStream
import java.io.OutputStream

abstract class Settings(namespace: String = "", input: InputStream? = null, serializer: SerializerStrategy = SerializerStrategy) : ParentSettingsNode() {

    @JvmField
    internal val container = MutablePersistentDataContainer().toBuffered()

    init {
        input?.let { container.loadFrom(it, serializer) }
    }

    final override val path: String = namespace

    final override val settings: Settings = this

    protected fun saveTo(output: OutputStream, serializer: SerializerStrategy = SerializerStrategy) {
        container.saveTo(output, serializer)
    }

    protected fun loadFrom(input: InputStream, serializer: SerializerStrategy = SerializerStrategy) {
        container.loadFrom(input, serializer)
    }

    protected fun getContainer(): BufferedPersistentDataContainer = container
}
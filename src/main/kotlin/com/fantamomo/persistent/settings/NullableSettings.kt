package com.fantamomo.persistent.settings

import java.util.Optional
import kotlin.jvm.optionals.getOrNull

fun <P : Any, C : Any> SettingsEntry<P, Optional<C>>.setNullable(value: C?) {
    this.value = Optional.ofNullable(value)
}

fun <P : Any, C : Any> SettingsEntry<P, Optional<C>>.getNullable(): C? = this.value.getOrNull()
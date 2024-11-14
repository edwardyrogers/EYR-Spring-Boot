package com.eyr.demo.core.kotlin.objects

import com.eyr.demo.core.kotlin.models.Meta

object RequestMetadata {
    private val value = ThreadLocal<Meta>()

    fun get(): Meta = run {
        value.get()
    }

    fun set(meta: Meta?) = run {
        if (meta != null) {
            value.set(meta)
        } else {
            value.set(Meta())
        }
    }

    fun clear() = run {
        value.remove()
        value.set(Meta())
    }
}
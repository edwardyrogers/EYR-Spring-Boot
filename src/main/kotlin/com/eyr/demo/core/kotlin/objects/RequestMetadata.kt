package cc.worldline.common.objects

import cc.worldline.common.models.Meta

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


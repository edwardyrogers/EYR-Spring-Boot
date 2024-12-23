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

    fun update(block: Meta.() -> Meta) = run {
        val currentMeta = get()
        val updatedMeta = currentMeta.block() // Apply the updates to the current Meta
        set(updatedMeta) // Save the updated Meta back to ThreadLocal
    }

    fun clear() = run {
        value.remove()
        value.set(Meta())
    }
}


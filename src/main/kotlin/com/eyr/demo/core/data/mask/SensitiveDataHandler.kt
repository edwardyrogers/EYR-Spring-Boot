package com.eyr.demo.core.data.mask

import com.eyr.demo.core.configs.MaskConfig

object SensitiveDataHandler {
    lateinit var maskConfig: MaskConfig

    fun doCheckAndMask(
        map: Map<*, *>,
    ): Map<*, *> = run {
        map.mapValues { (key, value) ->
            when (value) {
                is Map<*, *> -> doCheckAndMask(
                    value,
                )

                is String -> when (key) {
                    in maskConfig.hiddenTypes.all.fields -> maskAll(value)
                    in maskConfig.hiddenTypes.head.fields -> maskHead(value, value.length / 4)
                    in maskConfig.hiddenTypes.middle.fields -> maskMiddle(value, value.length / 4)
                    in maskConfig.hiddenTypes.tail.fields -> maskTail(value, value.length / 4)
                    in maskConfig.hiddenTypes.twoSides.fields -> maskTwoSides(value, value.length / 4)
                    else -> value
                }

                else -> value
            }
        }
    }

    fun maskAll(value: String): String = run {
        "*".repeat(value.length)
    }

    fun maskTail(value: String, visibleLen: Int): String = run {
        if (value.length < visibleLen || visibleLen <= 0) return@run value
        val visiblePart = value.take(visibleLen)
        val maskedPart = "*".repeat(value.length - visibleLen)
        "$visiblePart$maskedPart"
    }

    fun maskHead(value: String, visibleLen: Int): String = run {
        if (value.length < visibleLen || visibleLen <= 0) return@run value
        val visiblePart = value.substring(value.length - visibleLen)
        val maskedPart = "*".repeat(value.length - visibleLen)
        "$maskedPart$visiblePart"
    }

    fun maskMiddle(value: String, visibleLen: Int): String = run {
        if (value.length < visibleLen * 2 || visibleLen <= 0) return@run value
        val invisibleLen = value.length - (visibleLen * 2)
        val maskedPart = "*".repeat(invisibleLen)
        "${value.substring(0, visibleLen)}$maskedPart${value.substring(value.length - visibleLen)}"
    }

    fun maskTwoSides(value: String, visibleLen: Int): String = run {
        if (value.length < visibleLen * 2 || visibleLen <= 0) return@run value
        val invisibleLen = (value.length - visibleLen) / 2
        val maskedPart = "*".repeat(invisibleLen)
        "$maskedPart${value.substring(invisibleLen, value.length - invisibleLen)}$maskedPart"
    }
}
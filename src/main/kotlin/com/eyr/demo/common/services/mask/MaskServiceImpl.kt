package com.eyr.demo.common.services.mask

import com.eyr.demo.core.interfaces.MaskService
import org.springframework.stereotype.Service

@Service
class MaskServiceImpl : MaskService {
    override fun maskCustom(key: Any?, value: String): String = run {
        value
    }
}
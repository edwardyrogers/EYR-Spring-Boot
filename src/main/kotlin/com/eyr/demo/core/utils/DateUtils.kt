package com.eyr.demo.core.utils

import java.time.format.DateTimeFormatter

object DateUtils {
    fun dateTimeFormatter(with: String = "-"): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd${with}MM${with}yyyy HH:mm:ss")

    fun europeDateFormatter(with: String = "-"): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd${with}MM${with}yyyy")

    fun asianDateFormatter(with: String = "-"): DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy${with}MM${with}dd")
}

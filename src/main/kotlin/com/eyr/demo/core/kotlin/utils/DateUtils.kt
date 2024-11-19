package cc.worldline.common.utils

import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        fun dateTimeFormatter(with: String = "-"): DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd${with}MM${with}yyyy HH:mm:ss")

        fun europeDateFormatter(with: String = "-"): DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd${with}MM${with}yyyy")

        fun asianDateFormatter(with: String = "-"): DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy${with}MM${with}dd")
    }
}
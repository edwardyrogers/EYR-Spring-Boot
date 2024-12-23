package com.eyr.demo.core.constants

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * Represents the various channels through which a client may interact.
 *
 * This enum defines the following channels:
 * - UNKNOWN: Represents an unknown or unspecified client channel.
 * - MBK: Represents the Mobile Banking (MBK) client channel.
 * - IBK: Represents the Internet Banking (IBK) client channel.
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class ClientChannel {
    UNKNOWN,
    IBK,
    PWS,
    MBK,
    ASC,
    STAFF_CHANNEL
}
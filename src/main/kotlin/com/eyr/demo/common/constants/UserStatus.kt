package cc.worldline.customermanagement.v2.common.constants

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class UserStatus {
    ACTIVE, INACTIVE, REONBOARD, LOCKED
}
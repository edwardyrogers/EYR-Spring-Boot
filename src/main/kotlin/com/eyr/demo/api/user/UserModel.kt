package com.eyr.demo.api.user

import com.eyr.demo.business.user.UserProjector
import com.eyr.demo.core.interfaces.Payload
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.data.domain.Page

sealed class UserModel {

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = GetUsersREQ.All::class,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = GetUsersREQ.All::class, name = "ALL"),
    )
    sealed class GetUsersREQ(
        open val proj: String = ""
    ) : Payload {

        class All : GetUsersREQ()
    }

    data class GetUsersRES(
        val users: Page<out UserProjector>
    ) : Payload
}
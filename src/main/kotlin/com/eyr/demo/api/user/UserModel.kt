package com.eyr.demo.api.user

import com.eyr.demo.business.user.UserProjs
import com.eyr.demo.core.interfaces.Payload
import com.eyr.demo.datasource.user.UserRepoEntity
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
        JsonSubTypes.Type(value = GetUsersREQ.ByStatus::class, name = "STATUS"),
    )
    sealed class GetUsersREQ(
        open val proj: String = ""
    ) : Payload {
        class ByStatus(
            val status: UserRepoEntity.UserStatus
        ) : GetUsersREQ()

        class All : GetUsersREQ()
    }

    data class GetUsersRES(
        val users: Page<out UserProjs>
    ) : Payload
}
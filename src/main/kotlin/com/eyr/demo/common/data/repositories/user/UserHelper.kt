package com.eyr.demo.common.data.repositories.user

import com.eyr.demo.common.data.repositories.user.UserHelper.UserPermission.*
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserHelper {

    @JsonSerialize(using = UserRoleSerializer::class)
    enum class UserRole(
        private val permissions: Set<UserPermission>
    ) {
        STRANGER(
            emptySet()
        ),

        REGULAR(
            emptySet()
        ),

        ADMIN(
            setOf(
                ADMIN_CREATE,
                ADMIN_READ,
                ADMIN_UPDATE,
                ADMIN_DELETE,

                MANAGER_CREATE,
                MANAGER_READ,
                MANAGER_UPDATE,
                MANAGER_DELETE,
            )
        );

        val index get() = ordinal.toString()

        val authorities: MutableList<SimpleGrantedAuthority>
            get() = run {
                val authorities = permissions
                    .map { permission -> SimpleGrantedAuthority(permission.scope) }
                    .toMutableList()

                authorities.add(SimpleGrantedAuthority("ROLE_$name"))
                authorities.stream().toList()
            }
    }

    private class UserRoleSerializer : JsonSerializer<UserRole>() {
        override fun serialize(p0: UserRole?, p1: JsonGenerator?, p2: SerializerProvider?) {
            p1?.writeNumber(p0?.index)
        }
    }

    enum class UserPermission(
        val scope: String
    ) {
        ADMIN_CREATE("admin:create"),
        ADMIN_READ("admin:read"),
        ADMIN_UPDATE("admin:update"),
        ADMIN_DELETE("admin:delete"),

        MANAGER_CREATE("manager:create"),
        MANAGER_READ("manager:read"),
        MANAGER_UPDATE("manager:update"),
        MANAGER_DELETE("manager:delete");
    }
}
package com.eyr.demo.business.user

import com.eyr.demo.datasource.user.UserRepoEntity


sealed class UserProjs {
    companion object {
        fun fromString(
            name: String
        ): Class<out UserProjs> = run {
            when (name) {
                "INFO" -> UserForInfo::class.java
                else -> UserForAll::class.java
            }
        }
    }

    data class UserForAll(
        val id: Long = 0,
        val username: String,
        val email: String,
        val age: String,
        val phone: String,
        val status: UserRepoEntity.UserStatus
    ) : UserProjs()

    data class UserForInfo(
        val email: String,
        val age: String,
        val phone: String,
    ) : UserProjs()
}
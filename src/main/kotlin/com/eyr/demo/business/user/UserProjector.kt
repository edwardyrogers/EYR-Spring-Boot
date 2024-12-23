package com.eyr.demo.business.user


sealed class UserProjector {
    companion object {
        fun fromString(
            name: String
        ): Class<out UserProjector> = run {
            when (name) {
                "ALL" -> UserForAll::class.java
                else -> UserForAll::class.java
            }
        }
    }

    data class UserForAll(
        val id: Long = 0,
        val username: String,
        val email: String
    ) : UserProjector()
}
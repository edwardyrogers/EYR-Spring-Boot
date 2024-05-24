package com.eyr.demo.common.data.repositories.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserModel, Long?> {
    fun findByUsername(username: String?): UserModel?
}

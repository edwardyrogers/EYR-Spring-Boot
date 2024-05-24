package com.eyr.demo.common.data.repositories.user

import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository
)
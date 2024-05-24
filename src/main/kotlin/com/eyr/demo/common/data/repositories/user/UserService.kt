package com.eyr.demo.common.data.repositories.user

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.data.repositories.user.UserHelper.UserRole
import com.eyr.demo.common.exceptions.RequestFailedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun findByUsername(username: String): UserModel =
        userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found")

    fun create(username: String, password: String, role: Int): UserModel =
        runCatching {
            userRepository.save(
                UserModel(
                    username = username,
                    password = passwordEncoder.encode(password),
                    role = UserRole.entries[role],
                )
            )
        }.getOrElse {
            when (it) {
                is IndexOutOfBoundsException -> throw RequestFailedException(
                    code = ReturnCode.BODY_VALIDATION_FAILED, msg = "Role $role is out of bounds"
                )

                else -> throw RequestFailedException(
                    code = ReturnCode.GENERAL_ERROR
                )
            }
        }

}
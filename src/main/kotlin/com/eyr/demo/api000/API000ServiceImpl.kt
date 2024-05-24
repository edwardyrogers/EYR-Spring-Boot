package com.eyr.demo.api000

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.data.repositories.user.UserHelper
import com.eyr.demo.common.data.repositories.user.UserModel
import com.eyr.demo.common.data.repositories.user.UserRepository
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.filters.jwt.JwtService
import com.eyr.demo.common.models.ApiModel
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class API000ServiceImpl(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
) : API000Service {

    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return runCatching {
            val user = userRepository.save(
                UserModel(
                    username = request.username,
                    password = passwordEncoder.encode(request.password),
                    role = UserHelper.UserRole.entries[request.role.toInt()],
                )
            )

            ApiModel.Response(
                payload = API000Model.API000001RES(
                    user = user
                )
            )
        }.getOrElse {
            when (it) {
                is IllegalArgumentException -> throw RequestFailedException(
                    code = ReturnCode.BODY_VALIDATION_FAILED,
                    msg = "Role ${request.role} is out of constant"
                )

                else -> throw RequestFailedException(
                    code = ReturnCode.GENERAL_ERROR
                )
            }
        }
    }

    override fun api000002(request: API000Model.API000002REQ): ApiModel.Response<API000Model.API000002RES> {
        return run {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password,
                )
            )

            val user = userRepository.findByUsername(request.username) ?: throw throw RequestFailedException(
                code = ReturnCode.ACCESS_DENIED,
                msg = "User not found"
            )

            val accessToken = jwtService.genAccessToken(user = user)
            val refreshToken = jwtService.genRefreshToken(user = user)

            ApiModel.Response(
                payload = API000Model.API000002RES(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )
            )
        }
    }
}
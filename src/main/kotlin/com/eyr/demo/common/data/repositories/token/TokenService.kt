package com.eyr.demo.common.data.repositories.token

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.data.repositories.token.TokenHelper.TokenType.BEARER
import com.eyr.demo.common.data.repositories.user.UserModel
import com.eyr.demo.common.exceptions.RequestFailedException
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenRepository: TokenRepository
) {
    fun save(user: UserModel, token: String): TokenModel =
        tokenRepository.save(
            TokenModel(
                user = user,
                token = token,
                type = BEARER,
                revoked = false,
                expired = false,
            )
        )

    fun findByToken(token: String): TokenModel =
        tokenRepository.findByToken(token) ?: throw RequestFailedException(
            code = ReturnCode.ACCESS_DENIED,
            msg = "Token not found"
        )
}
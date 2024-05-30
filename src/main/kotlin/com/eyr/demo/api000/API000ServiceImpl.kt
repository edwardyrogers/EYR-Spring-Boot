package com.eyr.demo.api000

import com.eyr.demo.common.data.repositories.token.TokenService
import com.eyr.demo.common.data.repositories.user.UserService
import com.eyr.demo.common.filters.jwt.JwtService
import com.eyr.demo.common.models.ApiModel
import com.eyr.demo.common.services.CryptoService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class API000ServiceImpl(
    private val cryptoService: CryptoService,
    private val jwtService: JwtService,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager,
) : API000Service {

    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return run {
            val user = userService.create(
                username = request.username,
                password = request.password,
                role = request.role.toInt(),
            )

            ApiModel.Response(
                payload = API000Model.API000001RES(
                    user = user
                )
            )
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

            val user = userService.findByUsername(username = request.username)
            val accessToken = jwtService.genAccessToken(user = user)
            val refreshToken = jwtService.genRefreshToken(user = user)

//            tokenService.save(user = user, token = accessToken)

            ApiModel.Response(
                payload = API000Model.API000002RES(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )
            )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun api000003(request: API000Model.API000003REQ): ApiModel.Response<API000Model.API000003RES> {
        return run {
            val publicKey = cryptoService.genRSAKeyPair("your_random_string_here").first

            ApiModel.Response(
                payload = API000Model.API000003RES(
                    pubKey = Base64.encode(publicKey)
                )
            )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun api000004(request: API000Model.API000004REQ): ApiModel.Response<API000Model.API000004RES> {
        return run {
            val encryptedBytes = Base64.decode(request.data)

            ApiModel.Response(
                payload = API000Model.API000004RES(
                    data = String(
                        cryptoService.doRSADecryption(encryptedBytes)
                    )
                )
            )
        }
    }
}
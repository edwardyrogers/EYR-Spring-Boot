package com.eyr.demo.api000

import org.springframework.stereotype.Service

@Service
class API000ServiceImpl(
//    private val cryptoService: CryptoService,
//    private val jwtService: JwtService,
//    private val userService: UserService,
//    private val tokenService: TokenService,
//    private val authenticationManager: AuthenticationManager
) : API000Service {

//    override fun api000001(body: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
//        return run {
//            val user = userService.create(
//                username = body.username,
//                password = body.password,
//                role = body.role.toInt(),
//            )
//
//            ApiModel.Response(
//                payload = API000Model.API000001RES(
//                    user = user
//                )
//            )
//        }
//    }
//
//    override fun api000002(body: API000Model.API000002REQ): ApiModel.Response<API000Model.API000002RES> {
//        return run {
//            authenticationManager.authenticate(
//                UsernamePasswordAuthenticationToken(
//                    body.username,
//                    body.password,
//                )
//            )
//
//            val user = userService.findByUsername(username = body.username)
//            val accessToken = jwtService.genAccessToken(user = user)
//            val refreshToken = jwtService.genRefreshToken(user = user)
//
//            tokenService.save(user = user, token = accessToken)
//
//            ApiModel.Response(
//                payload = API000Model.API000002RES(
//                    accessToken = accessToken,
//                    refreshToken = refreshToken,
//                )
//            )
//        }
//    }
//
//    override fun api000003(body: API000Model.API000003REQ): ApiModel.Response<API000Model.API000003RES> {
//        return run {
//            ApiModel.Response(
//                payload = API000Model.API000003RES(
//                    pubKey = Base64.getEncoder().encodeToString(
//                        cryptoService.genOrGetRSAKeyPair().first
//                    )
//                )
//            )
//        }
//    }
//
//    override fun api000004(body: API000Model.API000004REQ): ApiModel.Response<API000Model.API000004RES> {
//        return run {
//            ApiModel.Response(
//                payload = API000Model.API000004RES(
//                    encryptedData = "Hello Frontend",
//                    decryptedData = body.data,
//                )
//            )
//        }
//    }
}
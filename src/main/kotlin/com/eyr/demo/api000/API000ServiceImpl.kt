package com.eyr.demo.api000

import com.eyr.demo.common.constants.AppErrorCode
import com.eyr.demo.common.data.repositories.user.UserHelper
import com.eyr.demo.common.data.repositories.user.UserModel
import com.eyr.demo.common.data.repositories.user.UserRepository
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import java.lang.IllegalArgumentException

@Service
class API000ServiceImpl : API000Service {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return run {
            ApiModel.Response(
                payload = API000Model.API000001RES(
                    result = "in"
                )
            )
        }
    }

    override fun api000002(request: API000Model.API000002REQ): ApiModel.Response<API000Model.API000002RES> {
        return runCatching {
            userRepository.save(
                UserModel(
                    username = request.username,
                    password = passwordEncoder.encode(request.password),
                    role = UserHelper.UserRole.entries[request.role.toInt()],
                )
            )

            ApiModel.Response(
                payload = API000Model.API000002RES(
                    result = true
                )
            )
        }.getOrElse {
            when (it) {
                is IllegalArgumentException -> throw RequestFailedException(
                    code = AppErrorCode.BODY_VALIDATION_FAILED,
                    msg = "Dance type ${request.role} is out of constant"
                )

                else -> ApiModel.Response(
                    payload = API000Model.API000002RES(
                        result = false
                    )
                )
            }
        }
    }
}
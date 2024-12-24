package com.eyr.demo.api.user

import com.eyr.demo.business.user.UserProjs
import com.eyr.demo.business.user.bizlogics.GetUsersBizLogic
import com.eyr.demo.core.models.Request
import com.eyr.demo.core.models.Response
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val _getUsersBizLogic: GetUsersBizLogic
) {
    @PostMapping("get-users")
//    @Operation(summary = "Retrieve customers with given condition")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200", description = "Success", content = [
//                    Content(
//                        mediaType = "application/json",
//                    )
//                ]
//            ),
//        ]
//    )
    fun getCustomers(
        @Valid @RequestBody body: Request<UserModel.GetUsersREQ>
    ): Response<UserModel.GetUsersRES> = run {
        val payload = body.payload

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ(
                payload,
                UserProjs.fromString(body.payload.proj)
            )
        )

        Response.success(
            UserModel.GetUsersRES(
                users = response.data
            )
        )
    }
}
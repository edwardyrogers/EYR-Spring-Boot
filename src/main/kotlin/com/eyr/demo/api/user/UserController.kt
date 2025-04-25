package com.eyr.demo.api.user

import com.eyr.demo.business.user.UserProjs
import com.eyr.demo.business.user.bizlogics.GetUsersBizLogic
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
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
        @Valid @RequestBody body: UserModel.GetUsersREQ
    ): ResponseEntity<UserModel.GetUsersRES> = run {

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ(
                body,
                UserProjs.fromString(body.proj)
            )
        )

        ResponseEntity.ok(
            UserModel.GetUsersRES(
                users = response.data
            )
        )
    }
}
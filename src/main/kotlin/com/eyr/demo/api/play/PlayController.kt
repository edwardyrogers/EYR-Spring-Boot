package com.eyr.demo.api.play

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.interfaces.Payload
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/play")
class PlayController {
    @PostMapping("test")
    fun test(
        @Valid @RequestBody body: PlayModel.TestREQ
    ): ResponseEntity<PlayModel.TestRES> = run {
        ResponseEntity.ok(
            PlayModel.TestRES(
                greeting = "Hello, ${body.username}.",
                cardNumber = body.sub.cardNumber,
                revealedPSW = body.sub.email,
            )
        )
    }

    @PostMapping("test-get")
    fun testGet(
        @Valid @RequestBody body: PlayModel.TestREQ
    ): ResponseEntity<Payload.Empty> = run {
        throw ServiceException(ReturnCode.GENERAL_ERROR)
    }
}
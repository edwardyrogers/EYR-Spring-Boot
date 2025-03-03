package com.eyr.demo.api.play

import com.eyr.demo.core.interfaces.Payload
import com.eyr.demo.core.models.Request
import com.eyr.demo.core.models.Response
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/play")
class PlayController {
    @PostMapping("test")
    fun test(
        @Valid @RequestBody body: Request<PlayModel.TestREQ>
    ): Response<PlayModel.TestRES> = run {
        Response.success(
            payload = PlayModel.TestRES(
                greeting = "Hello, ${body.payload.username}.",
                cardNumber = body.payload.sub.cardNumber,
                revealedPSW = body.payload.sub.email,
            )
        )
    }

    @GetMapping("test-get")
    fun testGet(
        @RequestParam test: String
    ): Response<Payload.Empty> = run {
        Response.success()
    }
}
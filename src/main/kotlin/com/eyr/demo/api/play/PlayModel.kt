package com.eyr.demo.api.play

import com.eyr.demo.core.interfaces.Payload

sealed class PlayModel {
    data class TestREQ(
        val nationalId: String,
        val username: String,
        val displayName: String,
        var age: Int,
        val sub: NestedTest,
    ) : Payload {
        data class NestedTest(
            val cardNumber: String,
            val email: String,
            val phone: String,
        )
    }


    data class TestRES(
        val greeting: String,
        val cardNumber: String,
        val revealedPSW: String,
    ) : Payload
}
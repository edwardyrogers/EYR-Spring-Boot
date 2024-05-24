package com.eyr.demo

import com.eyr.demo.api000.API000Model
import com.eyr.demo.api000.API000Service
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class DemoApplication(
    private val service: API000Service
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        service.api000001(
            request = API000Model.API000001REQ(
                username = "stranger",
                password = "password",
                role = "0"
            )
        )

        service.api000001(
            request = API000Model.API000001REQ(
                username = "user",
                password = "password",
                role = "1"
            )
        )

        service.api000001(
            request = API000Model.API000001REQ(
                username = "admin",
                password = "password",
                role = "2"
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


package com.eyr.demo

import com.eyr.demo.api000.API000Model
import com.eyr.demo.api000.API000Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class DemoApplication : CommandLineRunner {
    @Autowired
    lateinit var service: API000Service

    override fun run(vararg args: String?) {
        service.api000002(
            request = API000Model.API000002REQ(
                username = "user",
                password = "\$2a\$12\$yMX1IbCqMDpCiNaTjx17Fe3/16geftzCmUzBXmYeAPzjRarFGBBUe",
                role = "0"
            )
        )

        service.api000002(
            request = API000Model.API000002REQ(
                username = "admin",
                password = "\$2a\$12\$yMX1IbCqMDpCiNaTjx17Fe3/16geftzCmUzBXmYeAPzjRarFGBBUe",
                role = "1"
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


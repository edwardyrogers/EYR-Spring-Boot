package com.eyr.demo

import com.eyr.demo.datasource.user.UserRepoEntity
import com.eyr.demo.datasource.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class DemoApplication(
    private val userRepository: UserRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        userRepository.saveAll(
            listOf(
                UserRepoEntity(
                    username = "Edward",
                    email = "edward@email.com"
                ),
                UserRepoEntity(
                    username = "Iris",
                    email = "iris@email.com"
                )
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


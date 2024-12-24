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
                    email = "edward@email.com",
                    age = "36",
                    phone = "0900000000",
                    status = UserRepoEntity.UserStatus.ACTIVE
                ),
                UserRepoEntity(
                    username = "Iris",
                    email = "iris@email.com",
                    age = "36",
                    phone = "0911000000",
                    status = UserRepoEntity.UserStatus.ACTIVE
                ),
                UserRepoEntity(
                    username = "Ryan",
                    email = "ryan@email.com",
                    age = "11",
                    phone = "0922000000",
                    status = UserRepoEntity.UserStatus.INACTIVE
                )
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


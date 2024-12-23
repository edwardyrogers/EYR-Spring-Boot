package com.eyr.demo.datasource.user

import jakarta.persistence.*

@Entity
data class UserRepoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String
)
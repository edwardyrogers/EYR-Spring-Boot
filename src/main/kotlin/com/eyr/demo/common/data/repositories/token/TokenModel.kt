package com.eyr.demo.common.data.repositories.token

import com.eyr.demo.common.data.repositories.token.TokenHelper.TokenType
import com.eyr.demo.common.data.repositories.user.UserModel
import jakarta.persistence.*

@Entity
data class TokenModel(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Column(unique = true)
    val token: String = "",

    @Enumerated(EnumType.ORDINAL)
    val type: TokenType = TokenType.BEARER,

    val revoked: Boolean = false,

    val expired: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserModel = UserModel(),
)
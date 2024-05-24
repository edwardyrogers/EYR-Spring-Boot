package com.eyr.demo.common.data.repositories.token

import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<TokenModel, Long?> {
//    @Query(
//        value = """select t from Token t inner join User u on t.user.id = u.id where u.id = :id and (t.expired = false or t.revoked = false)"""
//    )
//    fun findAllValidTokenByUser(id: Int): List<TokenModel>?

    fun findByToken(token: String): TokenModel?
}
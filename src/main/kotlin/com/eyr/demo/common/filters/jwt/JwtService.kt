package com.eyr.demo.common.filters.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function


@Service
class JwtService : LogoutHandler {
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${application.security.jwt.access.expiration}")
    private val accessExpiration: Long = 0

    @Value("\${application.security.jwt.refresh.expiration}")
    private val refreshExpiration: Long = 0

    private val signInKey: Key
        get() = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(secretKey)
        )

    private fun getClaims(token: String): Claims = Jwts
        .parserBuilder()
        .setSigningKey(signInKey)
        .build()
        .parseClaimsJws(token)
        .body


    fun <T> getClaim(token: String, claimsResolver: Function<Claims, T>): T =
        claimsResolver.apply(getClaims(token))

    fun getUsername(token: String): String =
        getClaim(token) { obj: Claims -> obj.subject }

    private fun getClaimExpiration(token: String): Date =
        getClaim(token) { obj: Claims -> obj.expiration }

    private fun createToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long,
    ): String = Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expiration))
        .signWith(signInKey, SignatureAlgorithm.HS256)
        .compact()

    fun genAccessToken(user: UserDetails): String =
        createToken(mapOf(), user, accessExpiration)

    fun genAccessToken(claims: Map<String, Any>, user: UserDetails): String =
        createToken(claims, user, accessExpiration)

    fun genRefreshToken(user: UserDetails): String =
        createToken(mapOf(), user, refreshExpiration)

    fun isTokenValid(token: String, user: UserDetails): Boolean =
        getUsername(token) == user.username && !isTokenExpired(token)

    private fun isTokenExpired(token: String): Boolean =
        getClaimExpiration(token).before(Date())

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        println("Edward")
    }
}
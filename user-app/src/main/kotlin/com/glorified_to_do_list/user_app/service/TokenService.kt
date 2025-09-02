package com.glorified_to_do_list.user_app.service

import com.glorified_to_do_list.user_app.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenService(jwtProperties: JwtProperties) {
    private val secretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    fun generate(
        username: String,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(username)
            .issuedAt(Date())
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    fun extractUserName(token: String): String? = getAllClaims(token).subject
    fun isTokenExpired(token: String): Boolean = getAllClaims(token).expiration.before(Date())

    fun isValid(token: String, userDetails: UserDetails): Boolean =
        extractUserName(token) == userDetails.username && !isTokenExpired(token)

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser.parseClaimsJws(token).payload
    }
}
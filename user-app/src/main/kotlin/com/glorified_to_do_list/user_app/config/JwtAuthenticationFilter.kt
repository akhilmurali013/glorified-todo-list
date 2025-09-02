package com.glorified_to_do_list.user_app.config

import com.glorified_to_do_list.user_app.service.CustomUserDetailService
import com.glorified_to_do_list.user_app.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter


class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailService,
    private val tokenService: TokenService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader!!.extractToken()
        val username = tokenService.extractUserName(jwtToken)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if(tokenService.isValid(jwtToken, userDetails!!)) {
                updateContext(userDetails, request)
            }
        }

        filterChain.doFilter(request, response)

    }

    private fun updateContext(user: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun String?.doesNotContainBearerToken(): Boolean = this == null || !this.startsWith("Bearer ");

    private fun String.extractToken(): String = this.substringAfter("Bearer ")
}


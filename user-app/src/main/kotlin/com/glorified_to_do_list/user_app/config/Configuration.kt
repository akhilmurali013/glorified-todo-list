package com.glorified_to_do_list.user_app.config

import com.glorified_to_do_list.user_app.repository.UserCredentialsRepository
import com.glorified_to_do_list.user_app.service.CustomUserDetailService
import com.glorified_to_do_list.user_app.service.TokenService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class ApplicationConfig(
    private val userCredentialsRepository: UserCredentialsRepository
) {

    @Bean
    fun jwtAuthenticationFilter(
        userDetailsService: CustomUserDetailService,
        tokenService: TokenService
    ): JwtAuthenticationFilter =
        JwtAuthenticationFilter(userDetailsService, tokenService)

    @Bean
    fun userDetailsService(): UserDetailsService =
        CustomUserDetailService(userCredentialsRepository)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}

package com.glorified_to_do_list.user_app.service

import com.glorified_to_do_list.user_app.model.UserCredentials
import com.glorified_to_do_list.user_app.repository.UserCredentialsRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userCredentialsRepository: UserCredentialsRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        val userCredentials = userCredentialsRepository.findByUserName(username)
        return userCredentials?.toUserDetails()
    }

    private fun UserCredentials.toUserDetails(): UserDetails = User.builder()
        .username(this.userName)
        .password(this.passwordHash)
        .roles("USER")
        .build()
}

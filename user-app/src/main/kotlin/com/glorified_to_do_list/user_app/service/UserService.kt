package com.glorified_to_do_list.user_app.service

import com.glorified_to_do_list.user_app.config.JwtProperties
import com.glorified_to_do_list.user_app.dto.AuthenticationResponse
import com.glorified_to_do_list.user_app.dto.LoginRequest
import com.glorified_to_do_list.user_app.exceptions.InvalidCredentialsException
import com.glorified_to_do_list.user_app.exceptions.UserAlreadyExistsException
import com.glorified_to_do_list.user_app.model.User
import com.glorified_to_do_list.user_app.model.UserCredentials
import com.glorified_to_do_list.user_app.repository.UserCredentialsRepository
import com.glorified_to_do_list.user_app.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository,
    private val paswordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {
    private fun createUserCredential(userName: String, password: String, user: User): UserCredentials {
        // Check if credentials already exist for this user
        if (userCredentialsRepository.existsById(user.id!!)) {
            throw UserAlreadyExistsException("Credentials already exist for user id ${user.id}")
        }
        if (userCredentialsRepository.existsByUserName(userName)) {
            throw UserAlreadyExistsException("Username $userName already exists")
        }
        val userCredentials = UserCredentials(userName = userName, passwordHash = paswordEncoder.encode(password), user = user)
        return userCredentialsRepository.save(userCredentials)
    }
    private fun createUser(name: String, email: String): User {
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException("User with email $email already exists")
        }
        val newUser = User(name = name, email = email)
        return userRepository.save(newUser);
    }

    @Transactional
    fun signUpUser(email: String, name: String, password: String, userName: String): User {
        val user = createUser(name, email)
        val userCredentials = createUserCredential(userName, password, user)
        return userCredentials.user
    }

    fun authenticate(request: LoginRequest): AuthenticationResponse {
        val auth = authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.userName, request.password)
        )

        if (auth.isAuthenticated) {
            val accessToken = tokenService.generate(
                auth.name,
                Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
            )
            val refreshToken = tokenService.generate(
                auth.name,
                Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
            )
            return AuthenticationResponse(
                accessToken,
                refreshToken
            )
        }
        throw InvalidCredentialsException("Invalid credentials");
    }


}
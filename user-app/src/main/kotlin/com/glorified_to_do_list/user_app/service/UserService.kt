package com.glorified_to_do_list.user_app.service

import com.glorified_to_do_list.user_app.exceptions.UserAlreadyExistsException
import com.glorified_to_do_list.user_app.model.User
import com.glorified_to_do_list.user_app.model.UserCredentials
import com.glorified_to_do_list.user_app.repository.UserCredentialsRepository
import com.glorified_to_do_list.user_app.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository
) {
    fun createUserCredential(userName: String, password: String, user: User): UserCredentials {
        // Check if credentials already exist for this user
        if (userCredentialsRepository.existsById(user.id!!)) {
            throw IllegalStateException("Credentials already exist for user id ${user.id}")
        }
        if (userCredentialsRepository.existsByUserName(userName)) {
            throw IllegalStateException("Username $userName already exists")
        }
        val userCredentials = UserCredentials(userName = userName, passwordHash = password, user = user)
        return userCredentialsRepository.save(userCredentials)
    }
    fun createUser(name: String, email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException("User with email $email already exists")
        }
        val newUser = User(name = name, email = email)
        return userRepository.save(newUser);
    }

    @Transactional
    fun signUpUser(email: String, name: String, password: String, userName: String): User {
        val user = createUser(name, email, password)
        val userCredentials = createUserCredential(userName, password, user)
        return userCredentials.user
    }
}
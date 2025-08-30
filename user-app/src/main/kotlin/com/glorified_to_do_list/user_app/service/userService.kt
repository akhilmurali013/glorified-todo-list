package com.glorified_to_do_list.user_app.service

import com.glorified_to_do_list.user_app.exceptions.UserAlreadyExistsException
import com.glorified_to_do_list.user_app.model.User
import com.glorified_to_do_list.user_app.model.UserCredentials
import com.glorified_to_do_list.user_app.repository.UserCredentialsRepository
import com.glorified_to_do_list.user_app.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class userService(
    private val userRepository: UserRepository,
    private val userCredentialsRepository: UserCredentialsRepository
) {
    fun creteUserCredential(userName: String, password: String, user: User): UserCredentials {
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

    fun signUpUser(email: String, name: String, password: String, userName: String): User {
        val user = createUser(name, email, password)
        val userCredentials = creteUserCredential(userName, password, user)
        return userCredentials.user
    }
}
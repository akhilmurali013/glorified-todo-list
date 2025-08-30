package com.glorified_to_do_list.user_app.repository

import com.glorified_to_do_list.user_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
package com.glorified_to_do_list.user_app.repository

import com.glorified_to_do_list.user_app.model.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository

interface UserCredentialsRepository: JpaRepository<UserCredentials, Long>
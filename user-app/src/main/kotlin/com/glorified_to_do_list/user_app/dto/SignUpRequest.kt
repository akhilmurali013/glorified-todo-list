package com.glorified_to_do_list.user_app.dto

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String,
    val userName: String
)
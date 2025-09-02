package com.glorified_to_do_list.user_app.dto

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)

package com.glorified_to_do_list.user_app.restController

import com.glorified_to_do_list.user_app.model.User
import com.glorified_to_do_list.user_app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserSignUpController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<User> {
        val user = userService.signUpUser(
            email = request.email,
            name = request.name,
            password = request.password,
            userName = request.userName
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String,
    val userName: String
)
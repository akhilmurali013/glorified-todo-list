package com.glorified_to_do_list.user_app.restController

import com.glorified_to_do_list.user_app.dto.LoginRequest
import com.glorified_to_do_list.user_app.dto.SignUpRequest
import com.glorified_to_do_list.user_app.model.User
import com.glorified_to_do_list.user_app.service.TokenService
import com.glorified_to_do_list.user_app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class UserSignUpController(
    private val userService: UserService,
    private val tokenService: TokenService,
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

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, String>?> {
        try {
            val auth = userService.authenticate(request)
            println("********** auth: $auth **********");
            val refreshTokenCookie = ResponseCookie.from("refreshToken", auth.refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .build()

            return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(mapOf("accessToken" to auth.accessToken))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emptyMap())
        }
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<String> = ResponseEntity.ok("Healthy")

}




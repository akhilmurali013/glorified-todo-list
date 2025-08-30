package com.glorified_to_do_list.user_app.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class UserAlreadyExistsException(message: String): RuntimeException(message)
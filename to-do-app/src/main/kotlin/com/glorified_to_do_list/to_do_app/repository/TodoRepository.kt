package com.glorified_to_do_list.to_do_app.repository

import com.glorified_to_do_list.to_do_app.model.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository: JpaRepository<Todo, Long> {
    fun findByUserId(userId: Long): List<Todo>
}
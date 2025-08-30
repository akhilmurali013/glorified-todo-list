package com.glorified_to_do_list.to_do_app.restController

import com.glorified_to_do_list.to_do_app.dto.TodoRequest
import com.glorified_to_do_list.to_do_app.dto.TodoResponse
import com.glorified_to_do_list.to_do_app.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoController(
    private val todoService: TodoService
) {
    @PostMapping
    fun createTodo(@RequestBody todo: TodoRequest): ResponseEntity<TodoResponse> {
        return todoService.createTodo(todo).let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{userId}")
    fun getTodosByUser(@PathVariable userId: Long): ResponseEntity<List<TodoResponse>> {
        val todos = todoService.getTodosByUser(userId)
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody updated: TodoRequest): ResponseEntity<TodoResponse> {
        val response = todoService.updateTodo(id, updated)
        return if (response != null) ResponseEntity.ok(response) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Void> {
        return if (todoService.deleteTodo(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}/completed")
    fun toggleCompleted(@PathVariable id: Long, @RequestBody isCompleted: Boolean): ResponseEntity<TodoResponse> {
        val response = todoService.toggleCompleted(id, isCompleted)
        return if (response != null) ResponseEntity.ok(response) else ResponseEntity.notFound().build()
    }
}
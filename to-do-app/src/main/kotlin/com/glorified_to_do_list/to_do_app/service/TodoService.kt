package com.glorified_to_do_list.to_do_app.service

import com.glorified_to_do_list.to_do_app.dto.TodoRequest
import com.glorified_to_do_list.to_do_app.dto.TodoResponse
import com.glorified_to_do_list.to_do_app.dto.toTodo
import com.glorified_to_do_list.to_do_app.dto.toTodoResponse
import com.glorified_to_do_list.to_do_app.repository.TodoRepository
import org.springframework.stereotype.Service


@Service
class TodoService(
    private val todoRepository: TodoRepository
) {
    fun getTodosByUser(userId: Long): List<TodoResponse> {
        return todoRepository.findByUserId(userId).map { it.toTodoResponse() }
    }

    fun createTodo(todo: TodoRequest): TodoResponse {
        val newTodo = todo.toTodo()
        val saved = todoRepository.save(newTodo)
        return saved.toTodoResponse()
    }

    fun updateTodo(id: Long, updated: TodoRequest): TodoResponse? {
        val existing = todoRepository.findById(id)
        return if (existing.isPresent) {
            val todo = existing.get().copy(
                name = updated.name,
                description = updated.description,
                completed = updated.completed
            )
            todoRepository.save(todo).toTodoResponse()
        } else null
    }

    fun deleteTodo(id: Long): Boolean {
        return if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id)
            true
        } else false
    }

    fun toggleCompleted(id: Long, isCompleted: Boolean): TodoResponse? {
        val existing = todoRepository.findById(id)
        return if (existing.isPresent) {
            val todo = existing.get().copy(
                completed = isCompleted
            )
            todoRepository.save(todo).toTodoResponse()
        } else null
    }

}


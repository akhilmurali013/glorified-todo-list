package com.glorified_to_do_list.to_do_app.dto

import com.glorified_to_do_list.to_do_app.model.Todo

data class TodoRequest(
    val name: String,
    val description: String,
    val completed: Boolean = false,
)

fun TodoRequest.toTodo(): Todo = Todo(
    name = this.name,
    description = this.description,
    completed = this.completed
)
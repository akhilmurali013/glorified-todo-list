package com.glorified_to_do_list.to_do_app.dto

import com.glorified_to_do_list.to_do_app.model.Todo
import java.time.ZonedDateTime

data class TodoResponse(
    val id: Long? = null,
    val name: String,
    val description: String,
    val completed: Boolean = false,
    val createdDate: ZonedDateTime?,
    val lastModifiedDate: ZonedDateTime?,
)

fun Todo.toTodoResponse() = TodoResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    completed = this.completed,
    createdDate = this.createdDate,
    lastModifiedDate = this.lastModifiedDate
)

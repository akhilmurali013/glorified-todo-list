package com.glorified_to_do_list.to_do_app.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String,
    val completed: Boolean = false,
    var createdDate: ZonedDateTime? = null,
    var lastModifiedDate: ZonedDateTime? = null,
    val userId: Long? = null
) {
    @PrePersist
    fun prePersist() {
        // Set both dates on creation
        val now = ZonedDateTime.now()
        createdDate = now
        lastModifiedDate = now
    }

    @PreUpdate
    fun preUpdate() {
        // Only update lastModifiedDate on subsequent changes
        lastModifiedDate = ZonedDateTime.now()
    }
}

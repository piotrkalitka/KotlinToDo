package com.piotrkalitka.fluttertodo.jpa.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "due_date", nullable = true)
    var dueDate: LocalDateTime?,

    @Column(name = "completed", nullable = false)
    var completed: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    @JsonIgnore
    var parentTask: Task? = null,

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User

)

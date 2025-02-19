package com.piotrkalitka.fluttertodo.jpa.repository

import com.piotrkalitka.fluttertodo.jpa.entity.Task
import com.piotrkalitka.fluttertodo.jpa.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository: JpaRepository<Task, Long> {

    fun findAllByOwner(owner: User): List<Task>

}

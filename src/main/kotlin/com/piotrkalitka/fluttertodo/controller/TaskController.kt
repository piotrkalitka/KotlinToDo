package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.controller.dto.request.CreateTaskRequestBody
import com.piotrkalitka.fluttertodo.controller.dto.request.UpdateTaskRequestBody
import com.piotrkalitka.fluttertodo.jpa.entity.Task
import com.piotrkalitka.fluttertodo.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/task")
class TaskController @Autowired constructor(
    private val taskService: TaskService
) {

    @GetMapping("{id}")
    @PreAuthorize("@taskService.isTaskOwner(#taskId)")
    fun findTaskById(@PathVariable("id") taskId: Long): Task {
        return taskService.findTaskById(taskId)
    }

    @GetMapping
    fun getAllTasks(): List<Task> {
        return taskService.findAllTasksForUser()
    }

    @PostMapping
    fun addTask(@RequestBody requestBody: CreateTaskRequestBody): Task {
        return taskService.addTask(
            requestBody.title,
            requestBody.dueDate,
            requestBody.parentId
        )
    }

    @PatchMapping("{id}")
    @PreAuthorize("@taskService.isTaskOwner(#taskId)")
    fun updateTask(@PathVariable("id") taskId: Long, @RequestBody requestBody: UpdateTaskRequestBody): Task {
        return taskService.updateTask(
            taskId,
            requestBody.title,
            requestBody.dueDate,
            requestBody.completed,
            requestBody.parentId
        )
    }

    @DeleteMapping("{id}")
    @PreAuthorize("@taskService.isTaskOwner(#taskId)")
    fun removeTask(@PathVariable("id") taskId: Long): ResponseEntity<Any> {
        taskService.removeTask(taskId)
        return ResponseEntity.noContent().build()
    }

}

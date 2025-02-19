package com.piotrkalitka.fluttertodo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TaskNotFoundException(val taskId: Long) : RuntimeException("Task with id $taskId could not be found") {
}

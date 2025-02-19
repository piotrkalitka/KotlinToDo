package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.controller.dto.response.ExceptionResponse
import com.piotrkalitka.fluttertodo.exception.RefreshTokenExpiredException
import com.piotrkalitka.fluttertodo.exception.TaskNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(exception: TaskNotFoundException, request: WebRequest): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse("Task with id: ${exception.taskId} could not be found", LocalDateTime.now())
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RefreshTokenExpiredException::class)
    fun handleRefreshTokenExpiredException(exception: RefreshTokenExpiredException, request: WebRequest): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse("Refresh token already expired")
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

}

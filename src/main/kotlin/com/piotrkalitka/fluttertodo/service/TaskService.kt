package com.piotrkalitka.fluttertodo.service

import com.piotrkalitka.fluttertodo.exception.TaskNotFoundException
import com.piotrkalitka.fluttertodo.jpa.entity.Task
import com.piotrkalitka.fluttertodo.jpa.repository.TaskRepository
import com.piotrkalitka.fluttertodo.security.UserDetailsImpl
import com.piotrkalitka.fluttertodo.jpa.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(
    @Autowired val userService: UserService,
    @Autowired val taskRepository: TaskRepository
) {

    fun findTaskById(taskId: Long): Task {
        return taskRepository.findById(taskId).orElseThrow { TaskNotFoundException(taskId) }
    }

    fun findAllTasksForUser(): List<Task> {
        return taskRepository.findAllByOwner(getCurrentUser())
    }

    fun addTask(title: String, dueDate: LocalDateTime?, parentId: Long?): Task {
        val parent = if (parentId != null) findTaskById(parentId) else null
        val entity = Task(null, title, dueDate, false, parent, getCurrentUser())
        return taskRepository.save(entity)
    }

    fun updateTask(taskId: Long, newTitle: String?, newDueDate: LocalDateTime?, newCompleted: Boolean?, newParentId: Long?): Task {
        val task = findTaskById(taskId)

        newTitle?.let {
            task.title = newTitle
        }

        newParentId?.let {
            task.parentTask = findTaskById(newParentId)
        }

        newDueDate?.let {
            task.dueDate = newDueDate
        }

        newCompleted?.let {
            task.completed = newCompleted
        }

        return taskRepository.save(task)
    }

    fun removeTask(taskId: Long) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId)
        } else {
            throw TaskNotFoundException(taskId)
        }
    }



    fun isTaskOwner(taskId: Long): Boolean {
        val currentUser = getCurrentUser()
        val task = taskRepository.findById(taskId).orElseThrow { TaskNotFoundException(taskId) }
        return task.owner.id == currentUser.id
    }

    private fun getCurrentUser(): User {
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        return userService.getUserById(userDetails.id)
    }

}

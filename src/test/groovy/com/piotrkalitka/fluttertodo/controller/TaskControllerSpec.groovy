package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.BaseDatabaseSpec
import com.piotrkalitka.fluttertodo.controller.dto.request.CreateTaskRequestBody
import com.piotrkalitka.fluttertodo.controller.dto.request.LoginRequest
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Stepwise

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Stepwise
@AutoConfigureMockMvc
class TaskControllerSpec extends BaseDatabaseSpec {

    private final TEST_USER_USERNAME = "testuser"
    private final TEST_USER_PASSWORD = "testpassword"
    private final TEST_USER_EMAIL = "testuser@test.com"

    @Autowired
    MockMvc mockMvc

    static String accessToken
    static long firstTaskId
    static long nestedTaskId

    def "should log in for tasks authorization"() {
        given: "should log in as test user"
        def request = new LoginRequest(TEST_USER_USERNAME, TEST_USER_PASSWORD)

        when: "log in request is sent"
        def result = mockMvc.perform {
            post("/api/auth/login")
            .contentType("application/json")
            .content(new JsonBuilder(request).toString())
            .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString
        accessToken = new JsonSlurper().parseText(response).accessToken

        then:
        response.contains(TEST_USER_USERNAME)
        response.contains(TEST_USER_EMAIL)
    }

    def "should create first new task"() {
        given: "Task creation request"

        String taskTitle = "Task1"
        String dueDate = "2025-12-12T12:00:00"

        def requestBody = [
                title: taskTitle,
                dueDate: dueDate
        ]

        when: "The user sends task creation request"
        def result = mockMvc.perform {
            post("/task")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(requestBody))
            .header("Authorization", "Bearer " + accessToken)
            .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString
        firstTaskId = new JsonSlurper().parseText(response).id

        then:
        response.contains(taskTitle)
        response.contains(dueDate)
    }

    def "should retrieve created task"() {
        given: "Get task by id request"

        when: "Request is sent"
        def result = mockMvc.perform {
            get("/task/" + firstTaskId)
                    .header("Authorization", "Bearer " + accessToken)
                    .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString

        then:
        response.contains(firstTaskId.toString())
    }

    def "should create new nested task"() {
        given: "Task creation request with parent id"

        String taskTitle = "NestedTask"
        String dueDate = "2025-10-10T15:15:15"

        def request = [
                title: taskTitle,
                dueDate: dueDate
        ]

        when: "request is sent"
        def result = mockMvc.perform {
            post("/task")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(request))
            .header("Authorization", "Bearer " + accessToken)
            .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString

        then:
        response.contains(taskTitle)
        response.contains(dueDate)
        response.contains(firstTaskId.toString())
    }

    def "should retrieve both new tasks"() {
        given: "Get all tasks request"

        when: "Request is sent"
        def result = mockMvc.perform {
            get("/task")
            .header("Authorization", "Bearer " + accessToken)
            .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString

        then:
        response.contains(firstTaskId.toString())
        response.contains(nestedTaskId.toString())
    }

    def "should update task with new title and dueDate"() {
        given: "Update task request"

        String newTitle = "newTitle"
        String newDueDate = "2026-11-11T20:20:20"

        def request = [
                title: newTitle,
                dueDate: newDueDate
        ]

        when: "request is sent"
        def result = mockMvc.perform {
            patch("/task/" + firstTaskId)
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(request))
            .header("Authorization", "Bearer " + accessToken)
            .buildRequest(it)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString

        then:
        response.contains(newTitle)
        response.contains(newDueDate)
    }

    def "should remove first task"() {
        given: "Task removal request"

        when: "request is sent"
        def result = mockMvc.perform {
            delete("/task/" + firstTaskId)
            .header("Authorization", "Bearer " + accessToken)
            .buildRequest(it)
        }.andExpect {
            status().isNoContent()
        }.andReturn()
        then: ""
    }


}

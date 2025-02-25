package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.BaseDatabaseSpec
import com.piotrkalitka.fluttertodo.controller.dto.request.LoginRequest
import com.piotrkalitka.fluttertodo.controller.dto.request.RefreshTokenRequestBody
import com.piotrkalitka.fluttertodo.controller.dto.request.RegisterRequest
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Stepwise

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Stepwise
@AutoConfigureMockMvc
class AuthControllerSpec extends BaseDatabaseSpec {

    String username = "testuser"
    String password = "testpassword"
    String email = "testuser@test.com"

    static String refreshToken

    @Autowired
    MockMvc mockMvc

    def "should register new user"() {
        given: "A new user registration request"
        def request = new RegisterRequest(username, email, password)

        when: "The user sends the registration request"
        def result = mockMvc.perform {
            post("/api/auth/register")
            .contentType("application/json")
            .content(new JsonBuilder(request).toString())
            .buildRequest(null)
        }.andExpect {
            status().isCreated()
        }.andReturn()

        then: "The response should contain the created user"
        def response = result.response.contentAsString
        response.contains(username)
        response.contains(email)
    }

    def "should log in as the new user"() {
        given: "A login request"
        def request = new LoginRequest(username, password)

        when: "The user sends the log in request"
        def result = mockMvc.perform {
            post("/api/auth/login")
            .contentType("application/json")
            .content(new JsonBuilder(request).toString())
            .buildRequest(null)
        }.andExpect {
            status().isOk()
        }.andReturn()

        def response = result.response.contentAsString
        refreshToken = new JsonSlurper().parseText(response).refreshToken

        then:
        response.contains(username)
        response.contains(email)
        response.contains("ROLE_USER")
    }

    def "should refresh access token"() {
        given: "A refresh token request"
        def request = new RefreshTokenRequestBody(refreshToken)

        when: "The user send refresh token request"
        def result = mockMvc.perform {
            post("/api/auth/refresh")
            .contentType("application/json")
            .content(new JsonBuilder(request).toString())
            .buildRequest(null)
        }.andExpect {
            status().isOk()
        }.andReturn()

        then:
        def response = result.response.contentAsString
        response.contains(username)
        response.contains(email)
        response.contains("ROLE_USER")
    }


}

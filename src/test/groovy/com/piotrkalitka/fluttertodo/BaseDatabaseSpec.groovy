package com.piotrkalitka.fluttertodo

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
@Testcontainers
abstract class BaseDatabaseSpec extends Specification {

    @Shared
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword")

    static {
        postgres.start()
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url") { postgres.getJdbcUrl() }
        registry.add("spring.datasource.username") { postgres.getUsername() }
        registry.add("spring.datasource.password") { postgres.getPassword() }
        registry.add("spring.liquibase.enabled") { true }
    }

}

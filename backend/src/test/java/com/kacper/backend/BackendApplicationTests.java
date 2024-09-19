package com.kacper.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Default test class for the Spring Boot application
 * Spring Boot
 */
@SpringBootTest
class BackendApplicationTest {

    @Test
    void contextLoads() {
        BackendApplication application = new BackendApplication();
        assertThat(application).isNotNull();
    }

}

package com.example.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
class UnderboutApiApplicationTests {
    @Test
    fun contextLoads() {
    }

    @Test
    fun basicTest() {
        StepVerifier
            .create(Mono.just("1"))
            .expectNext("1")
            .verifyComplete()
    }
}

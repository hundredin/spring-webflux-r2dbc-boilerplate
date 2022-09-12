package com.example.api

import com.example.api.entities.Post
import com.example.api.repositories.AccountRepository
import com.example.api.repositories.PostRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier


class PostRepositoryTest(
    @Autowired private val databaseClient: DatabaseClient,
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val postRepository: PostRepository,
) : BaseRepositoryTest() {
    @AfterEach
    fun afterEach() {
        databaseClient
            .sql("delete from post")
            .fetch()
            .rowsUpdated()
            .block()

        databaseClient
            .sql("delete from account")
            .fetch()
            .rowsUpdated()
            .block()
    }

    @Test
    fun `test save post`() {
        val account = generateAccount()

        val posts: Flux<Post> = accountRepository.save(account)
            .flatMapMany {
                Flux.just(
                    Post(
                        subject = "this is subject",
                        content = "this is content",
                        account = it
                    ), Post(
                        subject = "this is subject",
                        content = "this is content",
                        account = it
                    )
                )
            }.flatMap { postRepository.save(it) }

        StepVerifier.create(posts)
            .expectNextCount(2)
            .verifyComplete()
    }
}
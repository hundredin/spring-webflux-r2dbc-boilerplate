package com.example.api

import com.example.api.entities.Account
import com.example.api.repositories.AccountRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import reactor.test.StepVerifier
import kotlin.random.Random


class AccountRepositoryTest (
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val databaseClient: DatabaseClient
): BaseRepositoryTest() {
    @AfterEach
    fun afterEach() {
        databaseClient
            .sql("delete from account")
            .fetch()
            .rowsUpdated()
            .block()
    }

    private fun insertAccount(id: Long): Account {
        val givenAccount = generateAccount(id)

        databaseClient.sql("INSERT INTO account (id, email, access_token, refresh_token) VALUES(:id, :email, :accessToken, :refreshToken)")
            .bind("id", id)
            .bind("email", givenAccount.email)
            .bind("accessToken", givenAccount.accessToken)
            .bind("refreshToken", givenAccount.refreshToken)
            .fetch()
            .rowsUpdated()
            .block()

        return givenAccount
    }

    @Test
    fun `test save`() {
        val account = generateAccount()
        accountRepository.save(account)
            .`as` { StepVerifier.create(it) }
            .expectNextCount(1)
            .verifyComplete()

        val selectOne = databaseClient
            .sql("""
                select * from account
                where email='${account.email}'
            """.trimIndent()
            )
            .map { row -> mapOf(
                "email" to row[1],
                "accessToken" to row[2],
                "refreshToken" to row[3]
            )}
            .one()

        StepVerifier
            .create(selectOne)
            .expectNext(mapOf(
                "email" to account.email,
                "accessToken" to account.accessToken,
                "refreshToken" to account.refreshToken
            ))
            .verifyComplete()
    }

    @Test
    fun `test update`() {
        val givenAccount = insertAccount(Random.nextLong())

        val account = givenAccount.apply {
            email = "updated@example.com"
        }

        accountRepository.save(account)
            .`as` { StepVerifier.create(it) }
            .expectNextCount(1)
            .verifyComplete()

        val expected = databaseClient
            .sql("select * from account where id = ${account.id}")
            .map { row -> Account(
                id = row[0] as Long,
                email = row[1] as String,
                accessToken = row[2] as String,
                refreshToken = row[3] as String
            )
            }.one()

        StepVerifier
            .create(expected)
            .expectNext(account)
            .verifyComplete()
    }

    @Test
    fun `test findAll`() {
        insertAccount(1)
        insertAccount(2)
        insertAccount(3)

        accountRepository.findAll()
            .`as` { StepVerifier.create(it) }
            .expectNextCount(3)
            .verifyComplete()
    }
}
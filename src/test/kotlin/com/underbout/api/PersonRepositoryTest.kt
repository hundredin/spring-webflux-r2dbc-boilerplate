package com.underbout.api

import com.underbout.api.configs.R2dbcConfiguration
import com.underbout.api.entities.Person
import com.underbout.api.repositories.PersonRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*
import kotlin.random.Random


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [R2dbcConfiguration::class])
class PersonRepositoryTest (
    @Autowired private val personRepository: PersonRepository,
    @Autowired private val databaseClient: DatabaseClient
) {

    @BeforeEach
    fun beforeEach() {
        databaseClient
            .sql("delete from person")
            .fetch()
            .rowsUpdated()
            .block()
    }

    private fun insertPerson(id: Long): Person {
        val givenPerson = generatePerson(id)

        databaseClient.sql("INSERT INTO person (id, name, age) VALUES(:id, :name, :age)")
            .bind("id", id)
            .bind("name", givenPerson.name)
            .bind("age", givenPerson.age)
            .fetch()
            .rowsUpdated()
            .block()

        return givenPerson
    }

    private fun generatePerson(id: Long? = null): Person {
        return Person(
            id = id,
            name = UUID.randomUUID().toString(),
            age = Random.nextInt()
        )
    }

    @Test
    fun test() {
        StepVerifier
            .create(Mono.just("1"))
            .expectNext("1")
            .verifyComplete()
    }

    @Test
    fun `test save`() {
        val person = generatePerson()
        personRepository.save(person)
            .`as` { StepVerifier.create(it) }
            .expectNextCount(1)
            .verifyComplete()

        val selectOne = databaseClient
            .sql("""
                select * from person
                where name='${person.name}' and age='${person.age}'
            """.trimIndent()
            )
            .map { row -> mapOf("name" to row[1], "age" to row[2]) }
            .one()

        StepVerifier
            .create(selectOne)
            .expectNext(mapOf("name" to person.name, "age" to person.age))
            .verifyComplete()
    }

    @Test
    fun `test update`() {
        val givenPerson = insertPerson(Random.nextLong())

        val person = givenPerson.copy(
            name = "updated",
            age = 100
        )

        personRepository.save(person)
            .`as` { StepVerifier.create(it) }
            .expectNextCount(1)
            .verifyComplete()

        val expected = databaseClient
            .sql("select * from person where id = ${person.id}")
            .map { row -> Person(row[0] as Long, row[1] as String, row[2] as Int) }
            .one()

        StepVerifier
            .create(expected)
            .expectNext(person)
            .verifyComplete()
    }

    @Test
    fun `test findAll`() {
        insertPerson(Random.nextLong())
        insertPerson(Random.nextLong())
        insertPerson(Random.nextLong())

        personRepository.findAll()
            .`as` { StepVerifier.create(it) }
            .expectNextCount(3)
            .verifyComplete()
    }
}
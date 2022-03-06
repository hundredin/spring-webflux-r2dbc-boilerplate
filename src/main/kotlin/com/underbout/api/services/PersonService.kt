package com.underbout.api.services

import com.underbout.api.entities.Person
import com.underbout.api.repositories.PersonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class PersonService(private val personRepository: PersonRepository) {

    @Transactional
    fun create(person: Person): Mono<Person> {
        return personRepository.save(person)
    }

    @Transactional(readOnly = true)
    fun getAll(): Flux<Person> {
        return personRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Mono<Person> {
        return personRepository.findById(id)
    }

    fun update(person: Person): Mono<Person> {
        return Mono.justOrEmpty(person.id)
            .flatMap { personRepository.findById(it) }
            .switchIfEmpty(Mono.error(RuntimeException("There is no person to update")))
    }

    fun delete(id: Long): Mono<Void> {
        return personRepository.deleteById(id)
    }

}
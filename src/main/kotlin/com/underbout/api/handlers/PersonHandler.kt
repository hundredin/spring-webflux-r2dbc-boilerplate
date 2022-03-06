package com.underbout.api.handlers

import com.underbout.api.entities.Person
import com.underbout.api.services.PersonService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono

@Component
class PersonHandler(val personService: PersonService) {
    fun all(serverRequest: ServerRequest): Mono<ServerResponse> {
        return personService.getAll()
            .collectList()
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun get(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return personService.get(id)
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun post(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(Person::class.java)
            .flatMap { personService.create(it) }
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
    }

    fun put(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(Person::class.java)
            .flatMap { personService.update(it) }
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
    }

    fun delete(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return personService.delete(id)
            .flatMap { status(HttpStatus.OK).bodyValue("DELETED") }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }
}
package com.underbout.api.handlers

import com.underbout.api.services.PersonService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class HelloHandler(val personService: PersonService) {
    fun hello(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(HttpStatus.OK).bodyValue("Hello World")
    }
}
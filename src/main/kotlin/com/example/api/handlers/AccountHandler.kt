package com.example.api.handlers

import com.example.api.entities.Account
import com.example.api.services.AccountService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono

@Component
class AccountHandler(val accountService: AccountService) {
    fun all(serverRequest: ServerRequest): Mono<ServerResponse> {
        return accountService.getAll()
            .collectList()
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun get(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return accountService.get(id)
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun post(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(Account::class.java)
            .flatMap { accountService.create(it) }
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
    }

    fun put(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return serverRequest.bodyToMono(Account::class.java)
            .flatMap { accountService.update(id, it) }
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
    }

    fun delete(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return accountService.delete(id)
            .flatMap { status(HttpStatus.OK).bodyValue("DELETED") }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }
}
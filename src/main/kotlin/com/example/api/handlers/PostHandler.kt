package com.example.api.handlers

import com.example.api.services.PostData
import com.example.api.services.PostService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono

@Component
class PostHandler(val postService: PostService) {
    fun all(serverRequest: ServerRequest): Mono<ServerResponse> {
        return postService.getAll()
            .collectList()
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun getAllByAccount(serverRequest: ServerRequest): Mono<ServerResponse> {
        val accountId = serverRequest.queryParam("accountId").orElseThrow {
            RuntimeException("test")
        }.toLong()

        return postService.getByAccountId(accountId)
            .collectList()
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun get(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return postService.get(id)
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }

    fun post(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(PostData::class.java)
            .flatMap { postService.create(it) }
            .flatMap { status(HttpStatus.OK).bodyValue(it) }
    }

    fun delete(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id").toLong()

        return postService.delete(id)
            .flatMap { status(HttpStatus.OK).bodyValue("DELETED") }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).bodyValue("Not Found"))
    }
}
package com.example.api

import com.example.api.handlers.AccountHandler
import com.example.api.handlers.PostHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Routers(
    private val accountHandler: AccountHandler,
    private val postHandler: PostHandler
) {
    @Bean
    fun routerHandler() = router {
        "/v1/accounts".nest {
            GET("", accountHandler::all)
            POST("", accountHandler::post)
            GET("/{id}", accountHandler::get)
            PUT("/{id}", accountHandler::put)
            DELETE("/{id}", accountHandler::delete)
        }

        "/v1/posts".nest {
            GET("", postHandler::all)
            GET("", { r -> r.queryParam("accountId").isPresent }, postHandler::getAllByAccount)
            POST("", postHandler::post)
            GET("/{id}", postHandler::get)
            DELETE("/{id}", postHandler::delete)
        }
    }
}
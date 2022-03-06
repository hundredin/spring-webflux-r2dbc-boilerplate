package com.underbout.api

import com.underbout.api.handlers.HelloHandler
import com.underbout.api.handlers.PersonHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.router

@Configuration
class Routers(
    private val helloHandler: HelloHandler,
    private val personHandler: PersonHandler
) {
    @Bean
    fun routerHandler() = router {
        GET("/v1/hello", helloHandler::hello)

        "/v1/person".nest {
            GET("", personHandler::all)
            POST("", personHandler::post)
            GET("{id}", personHandler::get)
            PUT("{id}", personHandler::put)
            DELETE("{id}", personHandler::delete)
        }
    }
}
package com.example.api.repositories

import com.example.api.entities.Account
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
interface AccountRepository : ReactiveCrudRepository<Account, Long> {
    @Query("select id, email, access_token, refresh_token from account c where c.email = :email")
    fun findByEmail(email: String): Flux<Account>
}
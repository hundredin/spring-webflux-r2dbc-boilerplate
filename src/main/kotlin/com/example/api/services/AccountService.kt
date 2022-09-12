package com.example.api.services

import com.example.api.entities.Account
import com.example.api.repositories.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional
    fun create(account: Account): Mono<Account> {
        return accountRepository.save(account)
    }

    @Transactional(readOnly = true)
    fun getAll(): Flux<Account> {
        return accountRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Mono<Account> {
        return accountRepository.findById(id)
    }

    fun update(id: Long, account: Account): Mono<Account> {
        return Mono.justOrEmpty(id)
            .flatMap { accountRepository.findById(it) }
            .map {
                it.apply {
                    email = account.email
                    accessToken = account.accessToken
                    refreshToken = account.refreshToken
                }
            }
            .flatMap { accountRepository.save(it) }
            .switchIfEmpty(Mono.error(RuntimeException("There is no person to update")))
    }

    fun delete(id: Long): Mono<Void> {
        return accountRepository.deleteById(id)
    }

}
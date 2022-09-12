package com.example.api.services

import com.example.api.entities.Post
import com.example.api.repositories.AccountRepository
import com.example.api.repositories.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class PostService(private val accountRepository: AccountRepository, val postRepository: PostRepository) {

    @Transactional
    fun create(postData: PostData): Mono<Post> {
        return accountRepository.findById(postData.accountId)
            .flatMap { postRepository.save(Post(postData.subject, postData.content, it)) }
    }

    @Transactional(readOnly = true)
    fun getAll(): Flux<Post> {
        return postRepository.findAllPosts()
    }

    @Transactional(readOnly = true)
    fun getByAccountId(accountId: Long): Flux<Post> {
        return postRepository.findByAccountId(accountId)
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Mono<Post> {
        return postRepository.findById(id)
    }

    fun delete(id: Long): Mono<Void> {
        return postRepository.deleteById(id)
    }
}
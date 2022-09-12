package com.example.api.repositories

import com.example.api.entities.Post
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PostRepository : ReactiveCrudRepository<Post, Long> {
    @Query(
        """
        SELECT 
          p.id AS id,
          p.subject AS subject,
          p.content AS content,
          a.id AS account_id, 
          a.email as email, 
          a.access_token as access_token, 
          a.refresh_token as refresh_token 
        FROM post p 
        JOIN account a ON p.account_id = a.id 
        WHERE p.account_id = :accountId
        """
    )
    fun findByAccountId(accountId: Long): Flux<Post>

    @Query(
        """
        SELECT 
          p.id AS id,
          p.subject AS subject,
          p.content AS content,
          a.id AS account_id, 
          a.email as email, 
          a.access_token as access_token, 
          a.refresh_token as refresh_token 
        FROM post p 
        INNER JOIN account a ON p.account_id = a.id 
        """
    )
    fun findAllPosts(): Flux<Post>
}
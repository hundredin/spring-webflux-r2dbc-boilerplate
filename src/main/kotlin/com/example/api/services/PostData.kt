package com.example.api.services

data class PostData(
    val subject: String,
    val content: String,
    val accountId: Long,
)
package com.example.api.entities

import org.springframework.data.annotation.Id

data class Post(
    var subject: String,
    var content: String,
    @org.springframework.data.annotation.Transient val account: Account,
    @Id val id: Long? = null,
): BaseEntity()
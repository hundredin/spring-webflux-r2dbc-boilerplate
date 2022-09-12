package com.example.api.entities

import org.springframework.data.annotation.Id

data class Account(
    @Id val id: Long? = null,
    var email: String,
    var accessToken: String,
    var refreshToken: String
): BaseEntity()
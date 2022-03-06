package com.underbout.api.entities

import org.springframework.data.annotation.Id

data class Person(
    @Id val id: Long? = null,
    val name: String,
    val age: Int
)
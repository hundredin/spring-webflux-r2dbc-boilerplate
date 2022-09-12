package com.example.api

import com.example.api.configs.R2dbcConfiguration
import com.example.api.entities.Account
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.*

@DataR2dbcTest
@ContextConfiguration(classes = [R2dbcConfiguration::class])
@ActiveProfiles("local")
open class BaseRepositoryTest {
    protected fun generateAccount(id: Long? = null): Account {
        return Account(
            id = id,
            email = "test@example.com",
            accessToken = UUID.randomUUID().toString(),
            refreshToken = UUID.randomUUID().toString()
        )
    }
}
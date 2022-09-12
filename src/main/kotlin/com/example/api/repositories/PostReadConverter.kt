package com.example.api.repositories

import com.example.api.entities.Account
import com.example.api.entities.Post
import io.r2dbc.spi.Row
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class PostReadConverter: Converter<Row, Post> {
    override fun convert(source: Row): Post {
        val account = Account(
            email = source.get("email") as String,
            accessToken = source.get("access_token") as String,
            refreshToken = source.get("refresh_token") as String,
            id = source.get("account_id") as Long
        )

        return Post(
            subject = source.get("subject") as String,
            content = source.get("content") as String,
            account = account,
            id = source.get("id") as Long
        )
    }
}
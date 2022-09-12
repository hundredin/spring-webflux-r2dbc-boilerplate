package com.example.api.repositories

import com.example.api.entities.Post
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter

@WritingConverter
class PostWriterConverter : Converter<Post, OutboundRow> {
    override fun convert(source: Post): OutboundRow = OutboundRow().apply {
        source.id?.let {
            put("id", Parameter.from(it))
        }
        put("subject", Parameter.from(source.subject))
        put("content", Parameter.from(source.subject))
        put("account_id", Parameter.from(source.account.id!!))
    }
}

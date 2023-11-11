package com.example.api.configs

import com.example.api.repositories.PostReadConverter
import com.example.api.repositories.PostWriterConverter
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.transaction.ReactiveTransactionManager


@Configuration
@EnableR2dbcRepositories(basePackages = ["com.example.api.repositories"])
@EnableR2dbcAuditing
class R2dbcConfiguration(
    @Value("\${datasource.host}") val host: String,
    @Value("\${datasource.username}") val username: String,
    @Value("\${datasource.password}") val password: String,
    @Value("\${datasource.database}") val database: String
) : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(host)
                .username(username)
                .password(password)
                .database(database)
                .build()
        )
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        return ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(ResourceDatabasePopulator())
        }
    }

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters: List<Converter<*, *>> = listOf(
            PostReadConverter(), PostWriterConverter()
        )

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters)
    }
}
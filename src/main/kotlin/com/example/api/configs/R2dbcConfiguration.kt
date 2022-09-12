package com.example.api.configs

import com.example.api.repositories.PostReadConverter
import com.example.api.repositories.PostWriterConverter
import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.h2.H2ConnectionOption
import io.r2dbc.spi.ConnectionFactory
import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator


@Configuration
@EnableR2dbcRepositories(basePackages = ["com.example.api.repositories"])
@EnableR2dbcAuditing
class R2dbcConfiguration : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory("testdb")
                .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                .property("DATABASE_TO_LOWER", "TRUE")
                .username("sa")
                .password("password")
                .build()
        )
    }

    @Bean
    fun h2TcpServer(): Server {
        return Server.createTcpServer().start()
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        return ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(ResourceDatabasePopulator())
        }
    }

    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters: List<Converter<*, *>> = listOf(
            PostReadConverter(), PostWriterConverter()
        )

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters)
    }
}
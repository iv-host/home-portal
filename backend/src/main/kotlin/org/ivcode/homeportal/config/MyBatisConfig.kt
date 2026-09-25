package org.ivcode.homeportal.config

import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.mapping.DatabaseIdProvider
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.InputStream

// --== Supported JDBC Drivers ==-- //
private const val DRIVER_H2 = "org.h2.Driver"
private const val DRIVER_MYSQL = "com.mysql.jdbc.Driver"
private const val DRIVER_MYSQL_2 = "com.mysql.cj.jdbc.Driver"
private const val DRIVER_POSTGRESQL = "org.postgresql.Driver"

@Configuration
public class MyBatisConfig {

    /**
     * Defines the database id based on the driver's class name.
     *
     * To define vendor specific sql, use the "databaseId" property in the mybatis annotations.
     *
     * @see [org.apache.ibatis.annotations.Select.databaseId]
     * @see [org.apache.ibatis.annotations.Insert.databaseId]
     * @see [org.apache.ibatis.annotations.Update.databaseId]
     * @see [org.apache.ibatis.annotations.Delete.databaseId]
     */
    @Bean
    public fun createDatabaseIdProvider(): DatabaseIdProvider =
        DatabaseIdProvider { dataSource ->
            when(val driver = (dataSource as HikariDataSource).driverClassName) {
                DRIVER_H2 -> "h2"
                DRIVER_MYSQL -> "mysql"
                DRIVER_MYSQL_2 -> "mysql"
                DRIVER_POSTGRESQL -> "postgresql"
                else -> throw IllegalArgumentException("unsupported driver: $driver")
            }
        }

    /**
     * Registers [BinaryStreamTypeHandler] as the handler for [InputStream], replacing MyBatis' default
     * BLOB-based handler so binary columns (image data) work across MySQL, H2, and PostgreSQL.
     */
    @Bean
    public fun mybatisConfigurationCustomizer(): ConfigurationCustomizer = ConfigurationCustomizer { configuration ->
        configuration.typeHandlerRegistry.register(InputStream::class.java, BinaryStreamTypeHandler::class.java)
    }
}
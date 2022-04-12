package eom.study.kafkapractice.configuration

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import java.time.Duration

class R2dbcConfiguration(
    @Value("\${mysql.host}") private val mysqlHost : String,
    @Value("\${mysql.port") private val mysqlPort : String,
    @Value("\${mysql.id") private val mysqlId : String,
    @Value("\${mysql.password") private val mysqlPassword : String, ) : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "mysql")
                .option(ConnectionFactoryOptions.HOST, mysqlHost)
                .option(ConnectionFactoryOptions.USER, mysqlId)
                .option(ConnectionFactoryOptions.PORT, mysqlPort.toInt())  // optional, default 3306
                .option(ConnectionFactoryOptions.PASSWORD, mysqlPassword) // optional, default null, null means has no password
                .option(ConnectionFactoryOptions.DATABASE, "study") // optional, default null, null means not specifying the database
                .option(ConnectionFactoryOptions.CONNECT_TIMEOUT, Duration.ofSeconds(3))
                .build()
        )
    }

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory) : ReactiveTransactionManager
            = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun transactionAwareConnectionFactoryProxy(connectionFactory: ConnectionFactory): TransactionAwareConnectionFactoryProxy {
        return TransactionAwareConnectionFactoryProxy(connectionFactory)
    }

    @Bean
    fun r2dbcClient(connectionFactory: ConnectionFactory) : DatabaseClient {
        return DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .namedParameters(true)
            .build()
    }

    @Bean
    fun transactionalOperator(transactionManager: ReactiveTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(transactionManager)
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        //for initialization sql
        /*
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("data.sql")))
        */
        initializer.setDatabasePopulator(populator)
        return initializer
    }
}
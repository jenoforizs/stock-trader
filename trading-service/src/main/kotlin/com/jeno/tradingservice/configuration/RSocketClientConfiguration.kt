package com.jeno.tradingservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer
import reactor.util.retry.Retry
import java.time.Duration


@Configuration
class RSocketClientConfiguration {

    @Bean
    fun connectorConfigurer() = RSocketConnectorConfigurer { c ->
        c.reconnect(retryStrategy())
    }

    private fun retryStrategy() = Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2))
        .doBeforeRetry { s -> println("Connection lost... Retry : " + s.totalRetriesInARow()) }
}

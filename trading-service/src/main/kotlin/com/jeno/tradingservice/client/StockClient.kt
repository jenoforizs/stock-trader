package com.jeno.tradingservice.client

import com.jeno.stockservice.dto.StockTickDto
import io.rsocket.transport.netty.client.TcpClientTransport
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import reactor.util.retry.Retry
import java.time.Duration

@Service
class StockClient(
    builder: RSocketRequester.Builder,
    connectorConfigurer: RSocketConnectorConfigurer,
    @Value("\${stock.service.host}") private val host: String,
    @Value("\${stock.service.port}") private val port: Int
) {

    private val requester = builder.rsocketConnector(connectorConfigurer)
        .transport(TcpClientTransport.create(host, port))

    private val stocksCurrentPrice = mutableMapOf<String, Int>()

    private val flux = this.requester.route("stock.ticks")
        .retrieveFlux(StockTickDto::class.java)
        .doOnNext { stocksCurrentPrice[it.code] = it.price }
        .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2)))
        .publish()
        .autoConnect(0)


    fun getCurrentStockPrice(stockSymbol: String) = stocksCurrentPrice.getOrDefault(stockSymbol, 0)

    fun getStockStream() = flux
}

package com.jeno.stockservice.controller

import com.jeno.stockservice.dto.StockTickDto
import io.rsocket.transport.netty.client.TcpClientTransport
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.test.StepVerifier

@SpringBootTest
class StockControllerTest {

    @Autowired
    lateinit var builder: RSocketRequester.Builder

    @Test
    fun stockPriceTest() {
        val requester = builder.transport(TcpClientTransport.create("localhost", 7070))

        val flux = requester.route("stock.ticks")
            .retrieveFlux(StockTickDto::class.java)
            .doOnNext { println(it) }
            .take(12)

        StepVerifier.create(flux)
            .expectNextCount(12)
            .verifyComplete()
    }
}

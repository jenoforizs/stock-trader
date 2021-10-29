package com.jeno.stockservice.service

import com.jeno.stockservice.dto.Stock
import com.jeno.stockservice.dto.StockTickDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class StockService {

    val AMZN = Stock("AMZN", 1000, 20)
    val AAPL = Stock("AAPL", 100, 3)
    val MSFT = Stock("MSFT", 200, 5)

    fun getStockPrice() = Flux.interval(Duration.ofSeconds(2))
        .flatMap {
            Flux.just(AMZN, AAPL, MSFT)
        }
        .map { StockTickDto(it.code, it.getPrice()) }
}

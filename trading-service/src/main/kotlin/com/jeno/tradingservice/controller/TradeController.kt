package com.jeno.tradingservice.controller

import com.jeno.stockservice.dto.StockTickDto
import com.jeno.tradingservice.client.StockClient
import com.jeno.tradingservice.dto.StockTradeRequest
import com.jeno.tradingservice.dto.StockTradeResponse
import com.jeno.tradingservice.service.TradeService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("stock")
class TradeController(val tradeService: TradeService, val stockClient: StockClient) {

    @GetMapping(value = ["tick/stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stockTicks(): Flux<StockTickDto> = stockClient.getStockStream()

    @PostMapping("trade")
    fun trade(@RequestBody request: Mono<StockTradeRequest>): Mono<ResponseEntity<StockTradeResponse>> = request.filter { it.quantity > 0 }
        .flatMap { tradeService.trade(it) }
        .map<ResponseEntity<StockTradeResponse>> { ResponseEntity.ok(it) }
        .defaultIfEmpty(ResponseEntity.badRequest().build())
}

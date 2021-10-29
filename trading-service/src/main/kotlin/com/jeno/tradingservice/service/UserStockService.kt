package com.jeno.tradingservice.service

import com.jeno.tradingservice.dto.StockTradeRequest
import com.jeno.tradingservice.dto.UserStockDto
import com.jeno.tradingservice.entity.UserStockEntity
import com.jeno.tradingservice.repository.UserStockRepository
import com.jeno.tradingservice.util.EntityDTOUtil
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserStockService(val stockRepository: UserStockRepository) {

    fun buyStock(request: StockTradeRequest): Mono<UserStockEntity> = stockRepository.findByUserIdAndStockSymbol(request.userId, request.stockSymbol)
        .defaultIfEmpty(EntityDTOUtil.toUserStock(request))
        .doOnNext {
            it.quantity += request.quantity
        }
        .flatMap {
            stockRepository.save(it)
        }

    fun sellStock(request: StockTradeRequest): Mono<UserStockEntity> = stockRepository.findByUserIdAndStockSymbol(request.userId, request.stockSymbol)
        .filter {
            it.quantity >= request.quantity
        }
        .doOnNext {
            it.quantity -= request.quantity
        }
        .flatMap {
            stockRepository.save(it)
        }

    fun getUserStocks(userId: String): Flux<UserStockDto> = stockRepository.findByUserId(userId)
        .map { EntityDTOUtil.toUserStockDto(it) }
}

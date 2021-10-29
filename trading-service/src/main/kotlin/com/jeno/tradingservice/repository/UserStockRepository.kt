package com.jeno.tradingservice.repository

import com.jeno.tradingservice.entity.UserStockEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserStockRepository : ReactiveMongoRepository<UserStockEntity, String> {

    fun findByUserIdAndStockSymbol(userId: String, stockSymbol: String): Mono<UserStockEntity>
    fun findByUserId(userId: String): Flux<UserStockEntity>
}

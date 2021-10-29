package com.jeno.tradingservice.service

import com.jeno.tradingservice.client.StockClient
import com.jeno.tradingservice.client.UserClient
import com.jeno.tradingservice.dto.StockTradeRequest
import com.jeno.tradingservice.dto.StockTradeResponse
import com.jeno.tradingservice.dto.TradeStatus
import com.jeno.tradingservice.dto.TradeType
import com.jeno.tradingservice.util.EntityDTOUtil
import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TradeService(
    val userStockService: UserStockService,
    val userClient: UserClient,
    val stockClient: StockClient
) {

    fun trade(tradeRequest: StockTradeRequest): Mono<StockTradeResponse> {
        val transactionRequest = EntityDTOUtil.toTransactionRequest(tradeRequest, estimatePrice(tradeRequest))

        val mono = if (tradeRequest.tradeType == TradeType.BUY) {
            buyStock(tradeRequest, transactionRequest)
        } else {
            sellStock(tradeRequest, transactionRequest)
        }

        return mono.defaultIfEmpty(EntityDTOUtil.toStockTradeResponse(tradeRequest, TradeStatus.FAILED, 0))
    }

    fun buyStock(tradeRequest: StockTradeRequest, transactionRequest: TransactionRequest): Mono<StockTradeResponse> = userClient.doTransaction(transactionRequest)
        .filter { it.status == TransactionStatus.COMPLETED }
        .flatMap { userStockService.buyStock(tradeRequest) }
        .map { EntityDTOUtil.toStockTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.amount) }

    fun sellStock(tradeRequest: StockTradeRequest, transactionRequest: TransactionRequest): Mono<StockTradeResponse> = userStockService.sellStock(tradeRequest)
        .flatMap { userClient.doTransaction(transactionRequest) }
        .map { EntityDTOUtil.toStockTradeResponse(tradeRequest, TradeStatus.COMPLETED, transactionRequest.amount) }

    private fun estimatePrice(tradeRequest: StockTradeRequest) = tradeRequest.quantity * stockClient.getCurrentStockPrice(tradeRequest.stockSymbol)

}

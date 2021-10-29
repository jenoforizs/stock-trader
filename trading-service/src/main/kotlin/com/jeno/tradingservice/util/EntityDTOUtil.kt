package com.jeno.tradingservice.util

import com.jeno.tradingservice.dto.*
import com.jeno.tradingservice.entity.UserStockEntity
import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionType

class EntityDTOUtil {

    companion object {

        fun toTransactionRequest(request: StockTradeRequest, amount: Int) = TransactionRequest(
            userId = request.userId,
            amount = amount,
            type = if (request.tradeType == TradeType.BUY) {
                TransactionType.CREDIT
            } else {
                TransactionType.DEBIT
            }
        )

        fun toStockTradeResponse(request: StockTradeRequest, tradeStatus: TradeStatus, price: Int) = StockTradeResponse(
            userId = request.userId,
            stockSymbol = request.stockSymbol,
            quantity = request.quantity,
            tradeType = request.tradeType,
            tradeStatus = tradeStatus,
            price = price
        )

        fun toUserStock(request: StockTradeRequest) = UserStockEntity(
            userId = request.userId,
            stockSymbol = request.stockSymbol
        )

        fun toUserStockDto(userStockEntity: UserStockEntity) = UserStockDto(
            id = userStockEntity.id!!,
            userId = userStockEntity.userId,
            stockSymbol = userStockEntity.stockSymbol,
            quantity = userStockEntity.quantity
        )
    }
}

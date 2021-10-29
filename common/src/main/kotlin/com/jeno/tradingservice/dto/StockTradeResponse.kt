package com.jeno.tradingservice.dto

data class StockTradeResponse(
    val userId: String,
    val stockSymbol: String,
    val quantity: Int,
    val tradeType: TradeType,
    val tradeStatus: TradeStatus,
    val price: Int
)

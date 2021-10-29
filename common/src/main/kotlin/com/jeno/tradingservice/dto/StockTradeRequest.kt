package com.jeno.tradingservice.dto

data class StockTradeRequest(val userId: String, val stockSymbol: String, val quantity: Int, val tradeType: TradeType)

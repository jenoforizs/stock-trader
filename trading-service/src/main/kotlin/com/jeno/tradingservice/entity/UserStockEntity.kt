package com.jeno.tradingservice.entity

import org.springframework.data.annotation.Id

data class UserStockEntity(@Id var id: String? = null, val userId: String, val stockSymbol: String, var quantity: Int = 0)

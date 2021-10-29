package com.jeno.stockservice.controller

import com.jeno.stockservice.service.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class StockController {

    @Autowired
    lateinit var service: StockService

    @MessageMapping("stock.ticks")
    fun stockPrice() = service.getStockPrice()
}

package com.jeno.tradingservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TradingServiceApplication

fun main(args: Array<String>) {
	runApplication<TradingServiceApplication>(*args)
}

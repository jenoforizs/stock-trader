package com.jeno.stockservice.dto

import java.util.concurrent.ThreadLocalRandom

data class Stock(val code: String, private var price: Int, val volatility: Int) {

    fun getPrice(): Int {
        updatePrice()
        return price
    }

    private fun updatePrice() {
        val random = ThreadLocalRandom.current().nextInt(-1 * volatility, volatility + 1)
        price += random
        price = price.coerceAtLeast(0)
    }
}

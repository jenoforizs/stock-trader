package com.jeno.tradingservice.controller

import com.jeno.tradingservice.client.UserClient
import com.jeno.tradingservice.dto.UserStockDto
import com.jeno.tradingservice.dto.UserStockEstimatedValueDto
import com.jeno.tradingservice.service.UserStockService
import com.jeno.userservice.dto.UserDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("user")
class UserController(val userClient: UserClient, val userStockService: UserStockService) {

    @GetMapping("all")
    fun getAllUsers(): Flux<UserDto> = userClient.getAllUsers()

    @GetMapping("{userId}")
    fun getOneUserByUserId(@PathVariable userId: String): Mono<UserDto> = userClient.getUserByUserId(userId)

    @GetMapping("{userId}/stocks")
    fun getAllStockByUserId(@PathVariable userId: String): Flux<UserStockDto> = userStockService.getUserStocks(userId)

    @GetMapping("{userId}/stocks/estimatedvalue")
    fun getEstimatedStockValueByUserId(@PathVariable userId: String): Flux<UserStockEstimatedValueDto> = userStockService.getUserStocksEstimatedValue(userId)
}

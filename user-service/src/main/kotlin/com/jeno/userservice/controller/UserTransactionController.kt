package com.jeno.userservice.controller

import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionResponse
import com.jeno.userservice.service.UserTransactionService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
@MessageMapping("user")
class UserTransactionController(val userTransactionService: UserTransactionService) {

    @MessageMapping("transaction")
    fun doTransaction(request: Mono<TransactionRequest>): Mono<TransactionResponse> = request.flatMap {
        userTransactionService.doTransaction(it)
    }
}

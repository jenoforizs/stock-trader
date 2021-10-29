package com.jeno.userservice.service

import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionResponse
import com.jeno.userservice.dto.TransactionStatus
import com.jeno.userservice.dto.TransactionType
import com.jeno.userservice.entity.UserEntity
import com.jeno.userservice.repository.UserRepository
import com.jeno.userservice.util.EntityDtoUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.function.UnaryOperator


@Service
class UserTransactionService {

    @Autowired
    lateinit var repository: UserRepository

    fun doTransaction(request: TransactionRequest): Mono<TransactionResponse> {
        val operation = if (request.type == TransactionType.CREDIT) {
            credit(request)
        } else {
            debit(request)
        }

        return repository.findById(request.userId)
            .transform(operation)
            .flatMap { repository.save(it) }
            .map { EntityDtoUtil.toResponse(request, TransactionStatus.COMPLETED) }
            .defaultIfEmpty(EntityDtoUtil.toResponse(request, TransactionStatus.FAILED))
    }

    private fun credit(request: TransactionRequest) = UnaryOperator<Mono<UserEntity>> { userMono: Mono<UserEntity> ->
        userMono
            .doOnNext { it.balance += request.amount }
    }


    private fun debit(request: TransactionRequest) = UnaryOperator<Mono<UserEntity>> { userMono: Mono<UserEntity> ->
        userMono
            .filter { it.balance >= request.amount }
            .doOnNext { it.balance -= request.amount }
    }
}

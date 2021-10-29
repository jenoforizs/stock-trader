package com.jeno.userservice.util

import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionResponse
import com.jeno.userservice.dto.TransactionStatus
import com.jeno.userservice.dto.UserDto
import com.jeno.userservice.entity.UserEntity


class EntityDtoUtil {

    companion object {
        fun toDto(userEntity: UserEntity) = UserDto(userEntity.id, userEntity.name, userEntity.balance)
        fun toEntity(userDto: UserDto) = UserEntity(userDto.id, userDto.name, userDto.balance)
        fun toResponse(request: TransactionRequest, status: TransactionStatus) = TransactionResponse(request.userId, request.amount, request.type, status)
    }
}

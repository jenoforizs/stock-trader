package com.jeno.userservice.dto

data class TransactionResponse(val userId: String, val amount: Int, val type: TransactionType, val status: TransactionStatus)

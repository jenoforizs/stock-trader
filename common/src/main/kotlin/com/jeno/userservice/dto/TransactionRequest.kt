package com.jeno.userservice.dto

data class TransactionRequest(val userId: String, val amount: Int, val type: TransactionType)

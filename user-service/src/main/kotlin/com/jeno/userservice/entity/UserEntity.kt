package com.jeno.userservice.entity

import org.springframework.data.annotation.Id

data class UserEntity(@Id var id: String? = null, val name: String, var balance: Int)

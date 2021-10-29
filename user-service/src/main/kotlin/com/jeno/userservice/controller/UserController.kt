package com.jeno.userservice.controller

import com.jeno.userservice.dto.UserDto
import com.jeno.userservice.service.UserService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
@MessageMapping("user")
class UserController(val userService: UserService) {

    // RS
    @MessageMapping("get.all")
    fun getAllUsers(): Flux<UserDto> = userService.getAllUsers()

    // RR
    @MessageMapping("get.{id}")
    fun getById(@DestinationVariable id: String): Mono<UserDto> = userService.getUserById(id)

    // RR
    @MessageMapping("create")
    fun createUser(user: Mono<UserDto>) = userService.createUser(user)

    //RR
    @MessageMapping("update.{id}")
    fun updateUser(@DestinationVariable id: String, user: Mono<UserDto>) = userService.updateUser(id, user)

    //FF
    @MessageMapping("delete.{id}")
    fun deleteUser(@DestinationVariable id: String): Mono<Void> = userService.deleteUser(id)
}

package com.jeno.userservice.service

import com.jeno.userservice.entity.UserEntity
import com.jeno.userservice.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class DataSetupService(val userRepository: UserRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val user1 = UserEntity(name = "Sam", balance = 10000)
        val user2 = UserEntity(name = "Mike", balance = 10000)
        val user3 = UserEntity(name = "Jake", balance = 10000)

        Flux.just(user1, user2, user3)
            .flatMap { userRepository.save(it) }
            .doOnNext { println("Saving... $it") }.subscribe{
                println("$it was saved.")
            }
    }
}

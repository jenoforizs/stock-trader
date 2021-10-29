package com.jeno.userservice.service

import com.jeno.userservice.dto.UserDto
import com.jeno.userservice.repository.UserRepository
import com.jeno.userservice.util.EntityDtoUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class UserService {

    @Autowired
    lateinit var repository: UserRepository

    fun getAllUsers() = repository.findAll()
        .map {
            EntityDtoUtil.toDto(it)
        }

    fun getUserById(userId: String) = repository.findById(userId).map {
        EntityDtoUtil.toDto(it)
    }

    fun createUser(mono: Mono<UserDto>): Mono<UserDto> {
        return mono
            .map {
                println("Creating user... $it")
                EntityDtoUtil.toEntity(it)
            }
            .flatMap {
                repository.save(it)
            }
            .map {
                EntityDtoUtil.toDto(it)
            }
    }

    fun updateUser(id: String, mono: Mono<UserDto>): Mono<UserDto> {
        return repository.findById(id)
            .flatMap {
                mono.map {
                    println("Updating user... $it")
                    EntityDtoUtil.toEntity(it)
                }.doOnNext {
                    it.id = id
                }
            }
            .flatMap {
                repository.save(it)
            }
            .map {
                EntityDtoUtil.toDto(it)
            }
    }

    fun deleteUser(id: String) = repository.deleteById(id)
}

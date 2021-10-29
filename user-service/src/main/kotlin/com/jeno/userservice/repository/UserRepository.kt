package com.jeno.userservice.repository

import com.jeno.userservice.entity.UserEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : ReactiveMongoRepository<UserEntity, String>

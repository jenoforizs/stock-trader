package com.jeno.userservice

import com.jeno.userservice.dto.UserDto
import io.rsocket.transport.netty.client.TcpClientTransport
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.test.StepVerifier

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName::class)
class UserCRUDTest {

    @Autowired
    lateinit var builder: RSocketRequester.Builder

    private lateinit var requester: RSocketRequester

    @BeforeAll
    fun setup() {
        requester = builder.transport(TcpClientTransport.create("localhost", 7071))
    }

    @Test
    fun test0001_getAllUsers() {
        val flux = requester.route("user.get.all")
            .retrieveFlux(UserDto::class.java)
            .doOnNext { println("Reading... $it") }

        StepVerifier.create(flux)
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    fun test0010_getOneUser() {
        val user = getRandomUser()

        //val mono = requester.route("user.get.{id}", user.id)
        val mono = requester.route("user.get.${user.id}")
            .retrieveMono(UserDto::class.java)
            .doOnNext { println("Reading... $it") }

        StepVerifier.create(mono)
            .expectNextMatches {
                it.id == user.id
            }
            .verifyComplete()
    }

    @Test
    fun test0020_deleteUser() {
        val allUsers = requester.route("user.get.all")
            .retrieveFlux(UserDto::class.java)
            .collectList().block()!!

        val user = allUsers.random()

        val mono = requester.route("user.delete.${user.id}").send()

        StepVerifier.create(mono)
            .verifyComplete()

        val flux = requester.route("user.get.all")
            .retrieveFlux(UserDto::class.java)
            .doOnNext { println("Reading... $it") }

        StepVerifier.create(flux)
            .expectNextCount(2)
            .verifyComplete()
    }

    @Test
    fun test0030_createUser() {
        val newUser = UserDto(name = "Alex", balance = 20000)

        val mono = requester.route("user.create")
            .data(newUser)
            .retrieveMono(UserDto::class.java)
            .doOnNext { println("Reading... $it") }

        StepVerifier.create(mono)
            .expectNextMatches {
                it.name == newUser.name
            }
            .verifyComplete()

    }

    @Test
    fun test0040_updateUser() {
        val user = getRandomUser()

        user.name = "Ivan"

        val mono = requester.route("user.update.${user.id}")
            .data(user)
            .retrieveMono(UserDto::class.java)
            .doOnNext { println("Reading... $it") }

        StepVerifier.create(mono)
            .expectNextMatches {
                it.name == "Ivan"
            }
            .verifyComplete()
    }

    private fun getRandomUser() = requester.route("user.get.all")
        .retrieveFlux(UserDto::class.java)
        .next()
        .block()!!
}

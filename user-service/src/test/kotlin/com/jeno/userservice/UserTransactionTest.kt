package com.jeno.userservice

import com.jeno.userservice.dto.*
import io.rsocket.transport.netty.client.TcpClientTransport
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.test.StepVerifier
import java.util.stream.Stream

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName::class)
class UserTransactionTest {
    @Autowired
    lateinit var builder: RSocketRequester.Builder

    private lateinit var requester: RSocketRequester

    @BeforeAll
    fun setup() {
        requester = builder.transport(TcpClientTransport.create("localhost", 7071))
    }

    @Test
    fun test0001_transaction_credit() {
        val user = getRandomUser()

        val request = TransactionRequest(user.id!!, 2000, TransactionType.BUY)

        val response = requester.route("user.transaction")
            .data(request)
            .retrieveMono(TransactionResponse::class.java)
            .doOnNext { println("Transaction response: $it") }

        StepVerifier.create(response)
            .expectNextMatches { it.status == TransactionStatus.COMPLETED }
            .verifyComplete()
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun test0010_transaction_debit(amount: Int, type: TransactionType, status: TransactionStatus) {
        val user = getRandomUser()

        val request = TransactionRequest(user.id!!, amount, type)

        val response = requester.route("user.transaction")
            .data(request)
            .retrieveMono(TransactionResponse::class.java)
            .doOnNext { println("Transaction response: $it") }

        StepVerifier.create(response)
            .expectNextMatches { it.status == status }
            .verifyComplete()
    }

    private fun testData() = Stream.of(
        Arguments.of(2000, TransactionType.BUY, TransactionStatus.COMPLETED),
        Arguments.of(2000, TransactionType.SELL, TransactionStatus.COMPLETED),
        Arguments.of(120000, TransactionType.SELL, TransactionStatus.FAILED),
    )

    private fun getRandomUser() = requester.route("user.get.all")
        .retrieveFlux(UserDto::class.java)
        .next()
        .block()!!
}

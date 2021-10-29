package com.jeno.tradingservice.client

import com.jeno.userservice.dto.TransactionRequest
import com.jeno.userservice.dto.TransactionResponse
import com.jeno.userservice.dto.UserDto
import io.rsocket.transport.netty.client.TcpClientTransport
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserClient(
    private val builder: RSocketRequester.Builder,
    private val connectorConfigurer: RSocketConnectorConfigurer,
    @Value("\${user.service.host}") private val host: String,
    @Value("\${user.service.port}") private val port: Int
) {

    private val requester = builder.rsocketConnector(connectorConfigurer)
        .transport(TcpClientTransport.create(host, port))

    fun doTransaction(request: TransactionRequest): Mono<TransactionResponse> = requester.route("user.transaction")
        .data(request)
        .retrieveMono(TransactionResponse::class.java)
        .doOnNext { println("doTransaction response: $it") }

    fun getAllUsers(): Flux<UserDto> = requester.route("user.get.all")
        .retrieveFlux(UserDto::class.java)

    fun getUserByUserId(userId: String): Mono<UserDto> = requester.route("user.get.$userId")
        .retrieveMono(UserDto::class.java)
}

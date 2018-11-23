package me.hltj

import io.ktor.application.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong


fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

val httpClient = HttpClient()

object MyLogger {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)
}

val myContext = newSingleThreadContext("MyOwnThread")

fun Application.module() {
    install(CallId) {
        retrieveFromHeader(HttpHeaders.XRequestId)

        generate { newRequestId() }

        verify { it.isNotEmpty() }
    }

    install(CallLogging) {
        callIdMdc("request-id")
    }

    routing {
        get("/") {
            val deferred = async(myContext) {
                try {
                    val result = httpClient.get<String>("http://localhost:8081/")
                    MyLogger.logger.info("success")
                    result
                } catch (e: Exception) {
                    MyLogger.logger.info("failure")
                    "-- nothing --"
                }
            }
            call.respondText(deferred.await())
        }
    }
}

private val lastIncrement = AtomicLong()
private fun newRequestId(): String = "hltj-me-${lastIncrement.incrementAndGet()}"




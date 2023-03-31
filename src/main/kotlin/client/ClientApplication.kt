package client

import SERVER_PORT
import UserOuterClass
import UserServiceGrpcKt
import id
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import paggingRequest
import user

private val channel by lazy { ManagedChannelBuilder.forAddress("localhost", SERVER_PORT).usePlaintext().build() }
private val stub by lazy { UserServiceGrpcKt.UserServiceCoroutineStub(channel) }

suspend fun main() {
    println("Launch client...")
    delay(1000)

    while (true) {
        println("Enter server method (get, insert, delete, getPagging). Enter 0 for exit")

        when (readln().lowercase().trim()) {
            "get" -> runCatching { get() }.onFailure { error("Error while getting: ${it.message}") }
            "insert" -> runCatching { insert() }.onFailure { error("Error while getting: ${it.message}") }
            "delete" -> runCatching { delete() }.onFailure { error("Error while getting: ${it.message}") }
            "getpagging" -> runCatching { getPagging() }.onFailure { error("Error while getting: ${it.message}") }
            "0" -> break
            else -> error("unknown command")
        }
    }

    println("Terminating client...")
    delay(1000)
}

private suspend fun get() {
    println(stub.get(id { id = readln("UserId:").toInt() }))
}

private suspend fun insert() {
    val result = stub.insert(
        user {
            lastname = readln("Lastname: ")
            firstname = readln("Firstname: ")
            middlename = readln("Middlename: ")
            age = readln("Age: ").toInt()
            gender = UserOuterClass.User.Gender.valueOf(readln("Gender (MALE, FEMALE):").trim())
        }
    )

    println("Succeeded: ${result.succeeded}, Message: ${result.message}, Error: ${result.error}")
}

private suspend fun delete() {
    val result = stub.delete(id { id = readln("UserId: ").toInt() })
    println("Succeeded: ${result.succeeded}, Message: ${result.message}, Error: ${result.error}")
}

private suspend fun getPagging() {
    val result = stub.getPagging(paggingRequest {
        pageLength = readln("Page Length: ").toInt()
        pageNum = readln("Page number: ").toLong()
    })

    if (result.error.isNotEmpty()) {
        println("Error: ${result.error}")
    } else {
        println(result.usersList)
    }
}

private fun error(message: String) {
    println(message)
}

private fun readln(message: String): String {
    println(message)
    return readln()
}
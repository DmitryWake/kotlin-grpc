import database.DatabaseFactory
import io.grpc.Server
import io.grpc.ServerBuilder
import service.UserService

const val SERVER_PORT = 50051

fun main() {
    DatabaseFactory.init()

    println("Building Server...")
    val server = buildServer()

    println("Starting Server...")
    server.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown Server...")
        server.shutdown()
    })

    println("Running...")
    server.awaitTermination()
}


private fun buildServer(): Server = ServerBuilder
    .forPort(SERVER_PORT)
    .addService(UserService.instance)
    .build()
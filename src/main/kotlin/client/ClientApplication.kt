package client

import UserOuterClass
import UserServiceGrpcKt
import io.grpc.ManagedChannelBuilder
import user

suspend fun main() {
    val port = 50051

    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    val stub = UserServiceGrpcKt.UserServiceCoroutineStub(channel)
    val data = user {
        lastname = "Ivanov"
        firstname = "Petr"
        middlename = "Sidorovich"
        age = 23
        gender = UserOuterClass.User.Gender.MALE
    }
    val result = stub.insert(data)
    print("Success is ${result.succeeded}")
}
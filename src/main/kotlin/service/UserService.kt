package service

import UserOuterClass
import UserServiceGrpcKt
import database.dao.UserDAOFacade
import database.model.Gender
import database.model.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import paggingUsersResult
import result
import user

class UserService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    private val userDao = UserDAOFacade.instance

    override suspend fun insert(request: UserOuterClass.User): UserOuterClass.Result = withContext(Dispatchers.IO) {
        request.toUserDTO().let { dto ->
            val inserted =
                runCatching { userDao.insertEntity(dto) }.onFailure { println(it) }.getOrNull()

            result {
                succeeded = inserted != null
                if (inserted == null) {
                    error = "Exception while creating user"
                } else {
                    message = "Inserted UserEntity: $inserted"
                }
            }
        }
    }

    override suspend fun get(request: UserOuterClass.Id): UserOuterClass.User = withContext(Dispatchers.IO) {
        runCatching { userDao.getEntity(request.id)?.toUser() }.onFailure { println(it) }.getOrNull()
            ?: createEmptyResponse()
    }

    override suspend fun delete(request: UserOuterClass.Id): UserOuterClass.Result = withContext(Dispatchers.IO) {
        val isSuccess = runCatching { userDao.deleteEntity(request.id) }.onFailure { println(it) }.getOrDefault(false)

        result {
            succeeded = isSuccess
            if (!isSuccess) {
                error = "Something went wrong"
            } else {
                message = "User with id ${request.id} successfully deleted"
            }
        }
    }

    override suspend fun getPagging(request: UserOuterClass.PaggingRequest): UserOuterClass.PaggingUsersResult =
        withContext(Dispatchers.IO) {
            val list =
                runCatching {
                    userDao.getPagging(
                        request.pageLength,
                        request.pageLength.toLong() * (request.pageNum - 1)
                    )
                }.getOrNull()

            paggingUsersResult {
                if (list == null) {
                    error = "something went wrong"
                }

                users.addAll(list.orEmpty().map { it.toUser() })
            }
        }

    private fun UserOuterClass.User.toUserDTO() = UserDTO(
        lastname = lastname,
        firstname = firstname,
        middlename = middlename,
        age = age.toUInt(),
        gender = Gender.valueOf(gender.name)
    )

    private fun UserDTO.toUser() = user {
        lastname = this@toUser.lastname
        firstname = this@toUser.firstname
        middlename = this@toUser.middlename
        age = this@toUser.age.toInt()
        gender = UserOuterClass.User.Gender.valueOf(this@toUser.gender.name)
    }

    private fun createEmptyResponse() = user {
        lastname = ""
        firstname = ""
        middlename = ""
        age = 0
        gender = UserOuterClass.User.Gender.UNRECOGNIZED
    }

    companion object {
        val instance by lazy { UserService() }
    }
}
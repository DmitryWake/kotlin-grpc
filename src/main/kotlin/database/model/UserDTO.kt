package database.model

data class UserDTO(
    val id: Int = -1,
    val lastname: String,
    val firstname: String,
    val middlename: String,
    val age: UInt,
    val gender: Gender,
)

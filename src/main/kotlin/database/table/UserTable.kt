package database.table

import database.model.Gender
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable() {
    val lastname = varchar("lastname", 30)
    val firstname = varchar("firstname", 30)
    val middlename = varchar("middlename", 30)
    val age = uinteger("age")
    val gender = enumeration("gender", Gender::class)
}
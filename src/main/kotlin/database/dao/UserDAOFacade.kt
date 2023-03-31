package database.dao

import database.DatabaseFactory.dbQuery
import database.model.UserDTO
import database.table.UserTable
import database.table.UserTable.lastname
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDAOFacade {

    suspend fun getEntity(id: Int): UserDTO? = dbQuery {
        UserTable.select { UserTable.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    suspend fun insertEntity(entity: UserDTO): UserDTO? = dbQuery {
        UserTable.insert {
            it[lastname] = lastname
            it[firstname] = firstname
            it[middlename] = middlename
            it[age] = age
            it[gender] = gender
        }.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    suspend fun deleteEntity(id: Int): Boolean = dbQuery {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }

    suspend fun getAll() = dbQuery { UserTable.selectAll().map(::resultRowToUser) }

    private fun resultRowToUser(row: ResultRow) = UserDTO(
        id = row[UserTable.id].value,
        lastname = row[lastname],
        firstname = row[UserTable.firstname],
        middlename = row[UserTable.middlename],
        age = row[UserTable.age],
        gender = row[UserTable.gender],
    )

    companion object {
        val instance by lazy { UserDAOFacade() }
    }
}
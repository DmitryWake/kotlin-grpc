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
            it[lastname] = entity.lastname
            it[firstname] = entity.firstname
            it[middlename] = entity.middlename
            it[age] = entity.age
            it[gender] = entity.gender
        }.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    suspend fun deleteEntity(id: Int): Boolean = dbQuery {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }

    suspend fun getAll(): List<UserDTO> = dbQuery { UserTable.selectAll().map(::resultRowToUser) }

    suspend fun getPagging(limit: Int, offset: Long): List<UserDTO> = dbQuery {
        UserTable.selectAll().limit(limit, offset).map(::resultRowToUser)
    }

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
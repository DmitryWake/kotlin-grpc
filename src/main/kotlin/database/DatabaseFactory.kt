package database

import database.table.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driver = "org.postgresql.Driver"
        val url = "jdbc:postgresql://localhost:5432/kotlin-grpc"
        val databasePassword = ""
        val user = "postgres"

        val database = Database.connect(url, driver, user, databasePassword)

        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}
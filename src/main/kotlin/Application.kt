import database.DatabaseFactory
import database.dao.UserDAOFacade

fun main() {
    DatabaseFactory.init()
    val userDao = UserDAOFacade.instance
}
package data.repository.dataSourceAbstraction

import logic.entities.User
import logic.entities.UserRole

interface AuthenticationDataSource{

    fun register(userName: String, password: String, userRole: UserRole): User

    fun save(user: User)

}
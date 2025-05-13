package logic.exceptions

open class PlanMateException(message: String) : Exception(message)

open class AuthenticationException(message: String) : PlanMateException(message)
class UserNotFoundException(message: String = "User not found") : AuthenticationException(message)
class InvalidLoginException(message: String = "Invalid login credentials") : AuthenticationException(message)
class UserExistsException(message: String = "User already exists") : AuthenticationException(message)

open class ProjectException(message: String) : PlanMateException(message)
class ProjectNotFoundException(message: String = "Project not found") : ProjectException(message)

open class TaskException(message: String = "Task operation failed") : PlanMateException(message)
class TaskAlreadyExistsException(message: String = "Task already exists") : TaskException(message)
class TaskNotFoundException(message: String = "Task not found") : TaskException(message)

open class StateException(message: String) : PlanMateException(message)
class InvalidStateNameException(message: String = "Invalid state name") : StateException(message)
class StateNotFoundException(message: String = "No State Found") : StateException(message)
class StateAlreadyExistException(message: String = "State Already Exists") : StateException(message)

// Validation exceptions
open class ValidationException(message: String = "Validation failed") : PlanMateException(message)
class InvalidInputException(message: String) : ValidationException(message)
class InvalidUserNameException(message: String = "Invalid username") : ValidationException(message)
class InvalidPasswordException(message: String = "Invalid password") : ValidationException(message)
class UnauthorizedUserException(message: String = "User is not authorized") : ValidationException(message)

class DataAccessException(message: String) : PlanMateException(message)

class DataSourceException(message: String) : IllegalArgumentException(message)
class EmptyHeaderException(message: String) : IllegalArgumentException(message)
class DataReadException(message: String) : RuntimeException(message)
class DataWriteException(message: String) : RuntimeException(message)
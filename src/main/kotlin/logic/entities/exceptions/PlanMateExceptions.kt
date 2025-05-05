package logic.entities.exceptions

open class PlanMateException(message: String) : Exception(message)

open class AuthenticationException(message: String) : PlanMateException(message)
class UserNotFoundException(message: String = "User not found") : AuthenticationException(message)
class InvalidLoginException(message: String = "Invalid login credentials") : AuthenticationException(message)
class UserAlreadyExistException(message: String = "User already exists") : AuthenticationException(message)

open class ProjectException(message: String) : PlanMateException(message)
class ProjectAlreadyExistsException(message: String = "Project already exists") : ProjectException(message)
class ProjectNotFoundException(message: String = "Project not found") : ProjectException(message)
class ProjectNameIsEmptyException(message: String = "Project name cannot be empty") : ProjectException(message)
class ProjectUnauthorizedUserException(message: String = "User Not Authorized") : ProjectException(message)

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
class MateUnauthorizedException(message: String = "User should be admin") : ValidationException(message)

class DataAccessException(message: String) : PlanMateException(message)

class CsvFileExceptions(message: String) : IllegalArgumentException(message)
class EmptyHeaderException(message: String) : IllegalArgumentException(message)
class CsvReadException(message: String) : RuntimeException(message)
class CsvWriteException(message: String) : RuntimeException(message)
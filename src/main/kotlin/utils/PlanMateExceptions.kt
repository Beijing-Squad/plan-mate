package utils

open class PlanMateException(message: String) : Exception(message)

open class AuthenticationException(message: String) : PlanMateException(message)
class UserNotFoundException(message: String) : AuthenticationException(message)
class InvalidLoginException(message: String) : AuthenticationException(message)

open class ProjectException(message: String) : PlanMateException(message)
class ProjectAlreadyExistsException(message: String) : ProjectException(message)
class ProjectNotFoundException(message: String) : ProjectException(message)

open class TaskException(message: String) : PlanMateException(message)
class TaskAlreadyExistsException(message: String) : TaskException(message)
class TaskNotFoundException(message: String) : TaskException(message)

class StateException(message: String) : PlanMateException(message)

open class ValidationException(message: String) : PlanMateException(message)
class InvalidInputException(message: String) : ValidationException(message)
class InvalidUserNameException(message: String) : ValidationException(message)
class InvalidPasswordException(message: String) : ValidationException(message)

class DataAccessException(message: String) : PlanMateException(message)
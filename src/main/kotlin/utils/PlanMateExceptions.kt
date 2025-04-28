package utils

open class PlanMateException(message: String) : Exception(message)
class AuthenticationException(message: String) : PlanMateException(message)
class ProjectException(message: String) : PlanMateException(message)
class TaskException(message: String) : PlanMateException(message)
class StateException(message: String) : PlanMateException(message)
class ValidationException(message: String) : PlanMateException(message)
class DataAccessException(message: String) : PlanMateException(message)
package logic.useCases.user

class ValidationUserUseCase {
    fun isUserNameBlank(userName: String) = userName.isBlank()
    fun isPasswordBlack(password: String) = password.isBlank()
}
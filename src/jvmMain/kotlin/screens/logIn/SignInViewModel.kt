package screens.logIn

import data.remote.services.sign.SignService
import data.remote.dto.SignInRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.skia.impl.Log
import utils.GlobalRole
import utils.ViewModel

class SignInViewModel(private val signService: SignService): ViewModel() {

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun checkFieldsForValid(
        inputLogin: String,
        inputPassword: String
    ) = inputLogin.length > 3 && inputPassword.length >= 6

    suspend fun authorizeUser(
        inputLogin: String,
        inputPassword: String
    ): String {
        val result = signService.getSignIn(SignInRequest(inputLogin, inputPassword))?.role ?: "null"
        Log.warn("role is $result")

        GlobalRole.role = result
        return result
    }

    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
    }
    fun onPasswordChange(newPassword: String){
        _password.value = newPassword
    }
}
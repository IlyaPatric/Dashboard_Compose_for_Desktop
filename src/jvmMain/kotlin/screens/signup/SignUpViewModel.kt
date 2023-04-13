package screens.signup

import data.remote.services.sign.SignService
import data.remote.dto.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.skia.impl.Log
import utils.GlobalRole
import utils.ViewModel

class SignUpViewModel(private val signService: SignService): ViewModel() {

    private val _firstname = MutableStateFlow("")
    val firstname = _firstname.asStateFlow()

    private val _secondname = MutableStateFlow("")
    val secondname = _secondname.asStateFlow()

    private val _lastname = MutableStateFlow("")
    val lastname = _lastname.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun checkFieldsForValid(
        inputFirstname: String,
        inputSecondname: String,
        inputLastname: String,
        inputLogin: String,
        inputPassword: String
    ): Boolean = inputLogin.length > 3 && inputPassword.length >= 6
            && inputFirstname.isNotEmpty() && inputSecondname.isNotEmpty()
            && inputLastname.isNotEmpty()

    suspend fun authorizedNewUser(
        inputFirstName: String,
        inputSecondName: String,
        inputLastName: String,
        inputLogin: String,
        inputPassword: String
    ): String {
        val role = signService.getSignUp(
            SignUpRequest(
                inputLogin,
                inputPassword,
                inputFirstName,
                inputSecondName,
                inputLastName
            )
        )?.role ?: "null"

        GlobalRole.role = role

        Log.warn("role is ${GlobalRole.role}")
        return role
    }

    fun onFirstnameChange(newFirstname: String){
        _firstname.value = newFirstname
    }
    fun onSecondnameChange(newSecondname: String){
        _secondname.value = newSecondname
    }
    fun onLastnameChange(newLastname: String) {
        _lastname.value = newLastname
    }
    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
    }
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }
}
package screens.logIn


import Screen2
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import navcontroller.NavController
import styles.errorColor
import styles.iconFieldColor
import styles.primaryColor
import styles.textColor


@Composable
@Preview
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel
) {

    val loginText by viewModel.login.collectAsState()
    val passwordText by viewModel.password.collectAsState()

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) {
        Icons.Default.Visibility
    } else {
        Icons.Default.VisibilityOff
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    backgroundColor = errorColor,
                    contentColor = Color.White,
                    elevation = 1.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = data.message, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Авторизация",
                style = TextStyle(
                    color = textColor,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(top = 64.dp)
            )
            Column(
                modifier = Modifier.padding(top = 160.dp).fillMaxWidth(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginText,
                    onValueChange = { viewModel.onLoginChange(it) },
                    label = { Text("Логин") },
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.onLoginChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (loginText.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Пароль") },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (passwordText.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Еще не зарегестрировались...",
                        style = TextStyle(
                            color = textColor.copy(0.5f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Text(
                        text = "Зарегистрироваться",
                        style = TextStyle(
                            color = primaryColor,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable {
                            navController.navigate(Screen2.SignUpScreen.name)
                        }
                    )
                }

                Button(
                    onClick = {
                        val valid = viewModel.checkFieldsForValid(loginText, passwordText)

                        if (valid){
                            viewModel.viewModelScope.launch {
                                val role = async {viewModel.authorizeUser( loginText, passwordText ) }.await()
                                if (role == "null") {
                                    launch {
                                        scaffoldState.snackbarHostState.showSnackbar("Такого пользователя не существует")
                                    }
                                } else {
                                    navController.navigate(Screen2.HomeNavigation.name)
                                }

                            }
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Неправильные введеные данные")
                            }
                        }

                    },
                    modifier = Modifier.padding(top = 56.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                ) {
                    Text(
                        text = "Авторизироваться",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


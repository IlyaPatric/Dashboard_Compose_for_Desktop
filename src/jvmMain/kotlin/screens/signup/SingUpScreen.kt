package screens.signup

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import navcontroller.NavController
import styles.errorColor
import styles.iconFieldColor
import styles.primaryColor
import styles.textColor
import utils.GlobalRole


@Composable
@Preview
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel
){

    val firstText by viewModel.firstname.collectAsState()
    val secondText by viewModel.secondname.collectAsState()
    val lastText by viewModel.lastname.collectAsState()
    val loginText by viewModel.login.collectAsState()
    val passwordText by viewModel.password.collectAsState()

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility)
        Icons.Default.Visibility
    else
        Icons.Default.VisibilityOff

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it){ data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    backgroundColor = errorColor,
                    contentColor = Color.White,
                    elevation = 1.dp,
                    shape = RoundedCornerShape(10.dp),
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
                text = "Регистрация",
                style = TextStyle(
                    color = textColor,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(top = 64.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 128.dp).fillMaxWidth(0.4f)
            ) {
                OutlinedTextField(
                    value = secondText,
                    onValueChange = { viewModel.onSecondnameChange(it) },
                    label = { Text("Фамилия") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onSecondnameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (secondText.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).padding(end = 12.dp)
                )
                OutlinedTextField(
                    value = firstText,
                    onValueChange = { viewModel.onFirstnameChange(it) },
                    label = { Text("Имя") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onFirstnameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (firstText.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).padding(end = 12.dp)
                )
                OutlinedTextField(
                    value = lastText,
                    onValueChange = { viewModel.onLastnameChange(it) },
                    label = { Text("Отчество ") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onLastnameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (lastText.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                modifier = Modifier.padding(top = 40.dp).fillMaxWidth(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginText,
                    onValueChange = { viewModel.onLoginChange(it) },
                    label = { Text("Логин ") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onLoginChange("") }) {
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
                    label = { Text("Пароль ") },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
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
                    modifier = Modifier.padding(top = 40.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { navController.navigateBack() },
                        modifier = Modifier.padding(end = 8.dp).border(2.dp, primaryColor, RoundedCornerShape(10.dp)),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = primaryColor
                        )
                    }

                    Button(
                        onClick = {
                            val valid =
                                viewModel.checkFieldsForValid(firstText, secondText, lastText, loginText, passwordText)
                            if (valid) {
                                viewModel.viewModelScope.launch {
                                    val role = viewModel.authorizedNewUser(
                                        firstText,
                                        secondText,
                                        lastText,
                                        loginText,
                                        passwordText
                                    )
                                    if (GlobalRole.role == "null") {
                                        launch {
                                            scaffoldState.snackbarHostState.showSnackbar("Неправильные введеные данные")
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
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                    ) {
                        Text(
                            text = "Зарегистрироваться",
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
}


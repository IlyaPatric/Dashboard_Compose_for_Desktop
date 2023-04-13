import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import androidx.compose.ui.graphics.painter.Painter
import data.remote.services.sign.SignService
import screens.*
import screens.logIn.SignInScreen
import screens.logIn.SignInViewModel
import screens.signup.SignUpScreen
import screens.signup.SignUpViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview
fun App() {
    val navController by rememberNavController(Screen2.SignInScreen.name)
    val signService = SignService.create()
    val viewModel = SignInViewModel(signService)
    val viewModelSecond = SignUpViewModel(signService)

    MaterialTheme {
        Scaffold(
            content = {
                CustomNavigationHost(
                    navController = navController,
                    viewModel = viewModel,
                    viewModelSecond = viewModelSecond
                )
            }
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun main() = application {

    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        val trayState = rememberTrayState()
        val notification = rememberNotification("Notification", "Message from App 'Hello!'")

        Tray(
            state = trayState,
            icon = TrayIcon,
            menu = {
                Item(
                    "Отправить уведомление",
                    onClick = {
                        trayState.sendNotification(notification)
                    }
                )
                Item(
                    "Выход",
                    onClick = ::exitApplication
                )
            }
        )

        Window(
            onCloseRequest = {
                isOpen = false
            },
            icon = MyAppIcon,
            //onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 1400.dp, height = 875.dp),
            resizable = false,
            title = "Dashboard car retail",
            undecorated = false
        ) {
            App()
        }
    }
}

/**
 * Иконка приложения и иконка в трее
 */
object MyAppIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFF57965C), Offset(size.width / 4, size.height / 4), Size(size.width / 2f, size.height / 2f))
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}



/**
 * Screens
 */
enum class Screen(
    val iconFilled: ImageVector
) {
    HomeScreen(
        iconFilled = Icons.Filled.Dashboard
    ),
    UserEditScreen(
        iconFilled = Icons.Filled.GroupAdd
    ),
    AddNewCarScreen(
        iconFilled = Icons.Filled.DirectionsCarFilled
    ),
    ReportScreen(
        iconFilled = Icons.Filled.Description
    )
}
enum class Screen2{
    SignUpScreen,
    SignInScreen,
    HomeNavigation
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun CustomNavigationHost(
    navController: NavController,
    viewModel: SignInViewModel,
    viewModelSecond: SignUpViewModel
) {

    NavigationHost(navController) {
        composable(Screen2.SignUpScreen.name) {
            SignUpScreen(
                navController = navController,
                viewModel = viewModelSecond
            )
        }
        composable(Screen2.SignInScreen.name) {
            SignInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen2.HomeNavigation.name) {
            HomeNavigation()
        }
    }.build()
}
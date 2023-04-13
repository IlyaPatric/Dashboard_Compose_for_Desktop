package navcontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * NavController class
 */

class NavController(
    private val startDestination: String,
    private var backStackScreens: MutableSet<String> = mutableSetOf()
) {
    // Переменная для хранение текущего экрана
    var currentScreen: MutableState<String> = mutableStateOf(startDestination)
    //var sourceScreen: MutableState<String> = mutableStateOf("")

    // Функция для навигации между экранами
    fun navigate(route: String, /*source: String*/ ) {
        if (route != currentScreen.value){
            if (backStackScreens.contains(currentScreen.value) && currentScreen.value != startDestination) {
                backStackScreens.remove(currentScreen.value)
            }

            if (route == startDestination) {
                backStackScreens = mutableSetOf()
            }
            else {
                backStackScreens.add(currentScreen.value)
            }
            currentScreen.value = route
            //sourceScreen.value = source
        }
    }

    // Функция для навигации назад
    fun navigateBack() {
        if (backStackScreens.isNotEmpty()){
            currentScreen.value = backStackScreens.last()
            backStackScreens.remove(currentScreen.value)
        }
    }
}

/**
 * Composable to remember the start of the navcontroller
 */

@Composable
fun rememberNavController(
    startDestination: String,
    backStackScreens: MutableSet<String> = mutableSetOf()
): MutableState<NavController> = rememberSaveable {
    mutableStateOf(NavController(startDestination, backStackScreens))
}
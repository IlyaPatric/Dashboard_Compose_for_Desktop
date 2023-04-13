package screens

import Screen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import screens.addcar.AddNewCarScreen
import screens.home.HomeScreen
import screens.report.ReportScreen
import screens.report.ReportViewModel
import screens.useredit.UserEditScreen
import styles.primaryColor
import styles.unselectedColor
import utils.GlobalRole

@ExperimentalFoundationApi
@Composable
fun HomeNavigation() {

    val screens = Screen.values().toList()
    val navController by rememberNavController(Screen.HomeScreen.name)
    val currentScreen by remember {
        navController.currentScreen
    }



    Scaffold(
        content = {
            Row {
                NavigationRail(
                    modifier = Modifier.align(Alignment.CenterVertically).fillMaxHeight().width(72.dp),
                    elevation = 2.dp
                ) {
                    screens.forEach {
                        if (GlobalRole.role == "Админ") {
                            NavigationRailItem(
                                selected = currentScreen == it.name,
                                icon = { Icon(imageVector = it.iconFilled, contentDescription = null) },
                                selectedContentColor = primaryColor,
                                unselectedContentColor = unselectedColor,
                                onClick = { navController.navigate(it.name) }
                            )
                        }
                        if (GlobalRole.role == "Обычный") {
                            if (it.name != "UserEditScreen") {
                                NavigationRailItem(
                                    selected = currentScreen == it.name,
                                    icon = { Icon(imageVector = it.iconFilled, contentDescription = null) },
                                    selectedContentColor = primaryColor,
                                    unselectedContentColor = unselectedColor,
                                    onClick = { navController.navigate(it.name) }
                                )
                            }
                        }
                    }
                }

                CustomNavigationHostSecond(navController = navController)
            }
        }
    )
}

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomNavigationHostSecond(
    navController: NavController
) {
    val listStateUserEdit = rememberLazyListState()
    val listStateAddAuto = rememberLazyListState()
    val reportViewModel = ReportViewModel()


    NavigationHost(navController){
        composable(Screen.HomeScreen.name){
            HomeScreen()
        }
        composable(Screen.UserEditScreen.name){
            UserEditScreen(listStateUserEdit)
        }
        composable(Screen.AddNewCarScreen.name){
            AddNewCarScreen(listStateAddAuto)
        }
        composable(Screen.ReportScreen.name){
            ReportScreen(reportViewModel)
        }
    }.build()
}
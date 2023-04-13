package screens.home


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import screens.CustomComponent
import styles.*
import screens.BarGraph as BarGraph1


@ExperimentalFoundationApi
@Composable
fun HomeScreen() {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    backgroundColor = successColor,
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
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ){

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        HorizontalCardItem(Icons.Outlined.Payments, "Средняя выручка", "216k ₽", "₽341k ↗", successColor, backgroundColorSuccess)
                        Divider(
                            color = Color(0xFF212325).copy(.3f),
                            modifier = Modifier.width(1.dp).height(65.dp).padding(vertical = 4.dp)
                        )
                        HorizontalCardItem(Icons.Outlined.CarRental, "Кол-во чего-то", "21k", "₽341k ↘", errorColor, backgroundColorError)
                        Divider(
                            color = Color(0xFF212325).copy(.3f),
                            modifier = Modifier.width(1.dp).height(65.dp).padding(vertical = 4.dp)
                        )
                        HorizontalCardItem(Icons.Outlined.TrendingUp, "Средний рейтинг", "88.98%", "34.56% ↗", successColor, backgroundColorSuccess)
                        Divider(
                            color = Color(0xFF212325).copy(.3f),
                            modifier = Modifier.width(1.dp).height(65.dp).padding(vertical = 4.dp)
                        )
                        HorizontalCardItem(Icons.Outlined.Timelapse, "Средняя время проката", "16д", "14д ↗", successColor, backgroundColorSuccess)
                    }
                }
            }

            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically
            ){
                // 2 уровень виджетов

                Card(
                    modifier = Modifier.weight(2f).fillMaxHeight().padding(end = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){
                        val dataList = mutableListOf(30,60,90,50,70,80,90,45,56,30,20,10)

                        val sumDataList by rememberSaveable{mutableStateOf(dataList.sum())}
                        Text(
                            text = "Общая выручка: ${sumDataList}k ₽",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColorLight
                            ),
                            modifier = Modifier.padding(start = 32.dp, bottom = 48.dp))

                        val floatValue = mutableListOf<Float>()
                        val datasList = mutableListOf("Янв","Феб","Мар","Апр","Май","Июн","Июл","Авг","Сен","Окт","Ноя","Дек")

                        dataList.forEachIndexed { index, value ->

                            floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())
                        }
                        BarGraph1(
                            graphBarData = floatValue,
                            xAxisScaleData = datasList,
                            barData_ = dataList,
                            height = 200.dp,
                            barWidth = 25.dp,
                            barColor = primaryColor,
                            barArrangement = Arrangement.SpaceEvenly
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp,

                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomComponent(indicatorValue = 88)
                    }
                }

            }
            Row(
                modifier = Modifier.weight(1f)
            ){
                // 3 уровень виджетов
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp
                ){
                    Row(
                        modifier = Modifier
                            .padding(vertical = 48.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        BottomHorizontalItem(icon = Icons.Outlined.Description, text = "Договор", description = "Договор об автопрокате", scaffoldState = scaffoldState)
                        BottomHorizontalItem(icon = Icons.Outlined.ContactPage, text = "Отчет о клиентах", description = "Отчет о кол-ве клиентах", scaffoldState = scaffoldState)
                        BottomHorizontalItem(icon = Icons.Outlined.Timer, text = "Отчет о длительности", description = "Отчет о средней длительности аренды", scaffoldState = scaffoldState)
                        BottomHorizontalItem(icon = Icons.Outlined.DirectionsCar, text = "Отчет об авто", description = "Отчет о кол-ве авто в прокате", scaffoldState = scaffoldState)
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalCardItem(
    icon: ImageVector,
    labelText: String,
    bodyText: String,
    bodyTextSmall: String,
    color: Color,
    backgroundColor: Color
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = icon,  //----------------
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(40.dp).padding(end = 8.dp)
            )
            Text(labelText, style = TextStyle( //-------------
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColorLight
            ))
        }
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(bodyText, style = TextStyle(  //------------------
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColorLight
            ))
            Text(bodyTextSmall, style = TextStyle( //---------
                    color = color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun BottomHorizontalItem(
    icon: ImageVector,
    text: String,
    description: String,
    viewModel: HomeViewModel = HomeViewModel(),
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()

    TooltipArea(
        tooltip = {
            Surface(
                color = Color.White,
                contentColor = primaryColor,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, primaryColor)
            ) {
                Text(
                    text = description,
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ){
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    viewModel.viewModelScope.launch {
                        val res = async{viewModel.getContract()}.await()
                        viewModel.getPDF(res)
                    }
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Файл создан по расположению D:/")
                    }
                }
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Text(
                        text = text,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = primaryColor,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen()
}
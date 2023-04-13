package screens.report

/*
*  Описание экрана составления Заявки
* */

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.skia.impl.Log
import styles.*
import utils.MaskTransformation

@Composable
fun ReportScreen(
    viewModel: ReportViewModel
) {

    val firstText by viewModel.firstname.collectAsState()
    val secondText by viewModel.secondname.collectAsState()
    val lastText by viewModel.lastname.collectAsState()
    val expDriving by viewModel.expDriving.collectAsState()


    val selectedRight by viewModel.selectedRight.collectAsState()
    var expandedRight by remember { mutableStateOf(false) }
    val rights = listOf("A", "B", "C", "D", "M", "A1", "B1", "C1", "D1", "BE", "CE", "DE", "C1E", "D1E")

    val selectedAuto by viewModel.selectedAuto.collectAsState()
    var expandedAuto by remember { mutableStateOf(false) }
    val autos = listOf("Mercedes-Benz  E63S AMG", "Tesla  Model Y", "Lada  Vesta")

    val dateStart by viewModel.dateStart.collectAsState()
    val dateEnd by viewModel.dateEnd.collectAsState()

    var dropDownWidth by remember { mutableStateOf(Size.Zero) }
    var labelHeightClient by remember { mutableStateOf(0f) }
    var labelHeightAuto by remember { mutableStateOf(0f) }
    var labelHeightDates by remember { mutableStateOf(0f) }
    val iconRight = if (expandedRight) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconAuto = if (expandedAuto) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var colorSnackBar = errorColor

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    backgroundColor = colorSnackBar,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Text(
                text = "Составление договора",
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColorLight
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 64.dp)
                    .fillMaxWidth()
                    .border(2.dp, primaryColor, RoundedCornerShape(10.dp))
                    .onGloballyPositioned { coordinates ->
                        labelHeightClient = coordinates.size.height.toFloat() + 14f
                    }
            ) {

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = secondText,
                        onValueChange = { it ->
                            viewModel.onSecondnameChange(it.filter {it.isLetter()})
                        },
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
                        onValueChange = { it ->
                            viewModel.onFirstnameChange(it.filter {it.isLetter()})
                        },
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
                        onValueChange = { it ->
                            viewModel.onLastnameChange(it.filter { it.isLetter() })
                        },
                        label = { Text("Отчество") },
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
                        modifier = Modifier.weight(1f).padding(end = 12.dp)
                    )

                    // Категория прав и стаж вождения
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedRight,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Категория прав") },
                            trailingIcon = {
                                IconButton(
                                    onClick = { expandedRight = !expandedRight }
                                ) {
                                    Icon(iconRight, null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    dropDownWidth = coordinates.size.toSize()
                                },
                            shape = RoundedCornerShape(10.dp)
                        )

                        DropdownMenu(
                            expanded = expandedRight,
                            onDismissRequest = { expandedRight = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                                .height(200.dp)
                        ) {
                            rights.forEach { right ->
                                DropdownMenuItem(
                                    onClick = {
                                        expandedRight = !expandedRight
                                        viewModel.onSelectedRightChange(right)
                                    }
                                ) {
                                    Text(text = right)
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = expDriving,
                        onValueChange = { it ->
                            if (it.length < 3) {
                                viewModel.onExpDrivingChange(it.filter { it.isDigit() })
                            }
                        },
                        label = { Text("Стаж") },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onExpDrivingChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (expDriving.isNotEmpty()) iconFieldColor else Color.Transparent
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Text(
                text = "Данные клиента",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(10.dp, (-labelHeightClient).dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.4f)
                    .border(2.dp, primaryColor, RoundedCornerShape(10.dp))
                    .onGloballyPositioned { coordinates ->
                        labelHeightAuto = coordinates.size.height.toFloat() + 14f
                    }
            ) {

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Авто
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedAuto,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Автомобиль") },
                            trailingIcon = {
                                IconButton(
                                    onClick = { expandedAuto = !expandedAuto }
                                ) {
                                    Icon(iconAuto, null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    dropDownWidth = coordinates.size.toSize()
                                },
                            shape = RoundedCornerShape(10.dp)
                        )

                        DropdownMenu(
                            expanded = expandedAuto,
                            onDismissRequest = { expandedAuto = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                                .height(200.dp)
                        ) {
                            autos.forEach { auto ->
                                DropdownMenuItem(
                                    onClick = {
                                        expandedAuto = !expandedAuto
                                        viewModel.onSelectedAutoChange(auto)
                                    }
                                ) {
                                    Text(text = auto)
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = "Авто",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(10.dp, (-labelHeightAuto).dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )



            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.5f)
                    .border(2.dp, primaryColor, RoundedCornerShape(10.dp))
                    .onGloballyPositioned { coordinates ->
                        labelHeightDates = coordinates.size.height.toFloat() + 14f
                    }
            ) {

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    OutlinedTextField(
                        value = dateStart,
                        onValueChange = {it ->
                            viewModel.onDateStartChange( it.filter { it.isDigit() } )
                        },
                        label = { Text("Дата начала") },
                        visualTransformation = MaskTransformation()
                    )

                    OutlinedTextField(
                        value = dateEnd,
                        onValueChange = {it ->
                            viewModel.onDateEndChange( it.filter { it.isDigit() } )
                        },
                        label = { Text("Дата окончания") },
                        visualTransformation = MaskTransformation()
                    )
                }
            }
            Text(
                text = "Даты проката",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(10.dp, (-labelHeightDates).dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val valid = viewModel.isValidFields(
                            firstText,
                            secondText,
                            lastText
                        )

                        if (valid) {
                            viewModel.viewModelScope.launch {
                                val res = async { viewModel.postReportToServer(
                                    firstText,
                                    secondText,
                                    lastText,
                                    selectedRight,
                                    expDriving,
                                    selectedAuto,
                                    dateStart,
                                    dateEnd
                                ) }.await()

                                if (res) {
                                    launch {
                                        colorSnackBar = successColor
                                        scaffoldState.snackbarHostState.showSnackbar("Удачно!")
                                    }
                                } else {
                                    launch {
                                        colorSnackBar = errorColor
                                        scaffoldState.snackbarHostState.showSnackbar("Ошибка!")
                                    }
                                }
                            }
                        } else {
                            scope.launch {
                                colorSnackBar = errorColor
                                scaffoldState.snackbarHostState.showSnackbar("Неправильные введеные данные")
                            }
                        }
                    },
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Составить",
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








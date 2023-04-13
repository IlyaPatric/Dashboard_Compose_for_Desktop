

package screens.addcar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import data.remote.models.Car
import kotlinx.coroutines.launch
import styles.*
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun AddNewCarScreen(
    listState: LazyListState
) {

    val scaffoldState = rememberScaffoldState()
    val colorSnackBar = remember { mutableStateOf(primaryColor) }

    val cars = remember { mutableStateListOf(
        Car(1, "Mercedes-Benz", "E63 AMG", "Универсал", "Автоматическая", 5, "Бизнес", 4500.00f, "Серый"),
        Car(2, "Tesla", "Model Y", "Лифтбэк", "Автоматическая", 4, "Эксклюзив", 3000.00f, "Красный"),
        Car(3, "Lada", "Vesta", "Седан", "Механическая", 4, "Обычный", 1500.00f, "Синий"),
    ) }

    val indexCar = remember { mutableStateOf(0) }
    val selectedBrand = remember { mutableStateOf("") }
    val selectedTypeBody = remember { mutableStateOf("") }
    val selectedTypeGear = remember { mutableStateOf("") }
    val textModel = remember { mutableStateOf("") }
    val selectedClass = remember { mutableStateOf("") }
    val selectedColor = remember { mutableStateOf("") }
    val textPrice = remember { mutableStateOf("") }

    var sliderPosition = remember { mutableStateOf(0f) }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it){ data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    snackbarData = data,
                    backgroundColor = colorSnackBar.value,
                    contentColor = Color.White,
                    actionColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    elevation = 1.dp
                )
            }
        },
        backgroundColor = primaryColor.copy(0.05f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1.5f)
            ) {
                HeaderEditField(
                    cars, scaffoldState, selectedBrand,
                    selectedTypeBody, selectedTypeGear, textModel,
                    selectedClass, selectedColor, textPrice, indexCar, colorSnackBar, sliderPosition
                )
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {
                BottomPreviewField(
                    listState, cars, indexCar,
                    selectedBrand, selectedTypeBody, selectedTypeGear,
                    textModel, selectedClass, selectedColor, textPrice, sliderPosition
                )
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun BottomPreviewField(
    listState: LazyListState,
    cars: SnapshotStateList<Car>,
    indexCar: MutableState<Int>,
    selectedBrand: MutableState<String>,
    selectedTypeBody: MutableState<String>,
    selectedTypeGear: MutableState<String>,
    textModel: MutableState<String>,
    selectedClass: MutableState<String>,
    selectedColor: MutableState<String>,
    textPrice: MutableState<String>,
    sliderPosition: MutableState<Float>
) {
    val headers = listOf("Бренд","Модель", "Тип кузова", "Тип КПП", "Кол-во мест", "Класс авто", "Стоимость, ₽", "Цвет")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            headers.forEach {header->
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        header,
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
        ) {
            itemsIndexed(cars) {index, car ->
                CarItem(car) {
                    indexCar.value = index
                    selectedBrand.value = car.brand
                    selectedTypeBody.value = car.typeBody
                    selectedTypeGear.value = car.typeGear
                    textModel.value = car.model
                    sliderPosition.value = car.countSeats.toFloat()
                    selectedClass.value = car.clas
                    selectedColor.value = car.color
                    textPrice.value = car.coast.toString()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CarItem(
    car: Car,
    onClick: () -> Unit
) {

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { 48.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx * 4 to 1)

    val colorByName = mapOf(
        "Красный" to Color.Red,
        "Синий" to Color.Blue,
        "Серый" to Color.Gray,
        "Желтый" to Color.Yellow,
        "Оранжевый" to Color(0xFFffa114)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = {_, _ -> FractionalThreshold(0.3f)},
                orientation = Orientation.Horizontal
            )
    ) {

        Row(Modifier.padding(start = 24.dp).align(Alignment.CenterStart)) {
            // изменить
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF41AE07).copy(0.1f))
            ){
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color(0xFF41AE07)
                )
            }
        }

        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth(),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.brand)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.model)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.typeBody)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.typeGear)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.countSeats.toString())
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.clas)
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(car.coast.toString())
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colorByName[car.color]!!)
                    )
                }
            }
        }
    }

}

@Composable
fun HeaderEditField(
    cars: SnapshotStateList<Car>,
    scaffoldState: ScaffoldState,
    selectedBrand: MutableState<String>,
    selectedTypeBody: MutableState<String>,
    selectedTypeGear: MutableState<String>,
    textModel: MutableState<String>,
    selectedClass: MutableState<String>,
    selectedColor: MutableState<String>,
    textPrice: MutableState<String>,
    indexCar: MutableState<Int>,
    colorSnackBar: MutableState<Color>,
    sliderPosition: MutableState<Float>
) {



    var expandedBrand by remember { mutableStateOf(false) }
    var expandedTypeBody by remember { mutableStateOf(false) }
    var expandedTypeGear by remember { mutableStateOf(false) }
    var expandedClass by remember { mutableStateOf(false) }
    var expandedColor by remember { mutableStateOf(false) }

    val brands = listOf("Mercedes-Benz", "Lada", "Tesla", "Range Rover")
    val typesBody = listOf("Хетчбэк", "Седан", "Лифтбэк", "Универсал", "Купе", "Кроссовер", "Внедорожник", "Минивэн")
    val typesGear = listOf("Механическая", "Автоматическая", "Робот", "Вариатор")
    val typeClass = listOf("Бизнес", "Эксклюзив", "Обычный")
    val colors = listOf("Красный","Синий","Серый","Желтый","Оранжевый")

    var dropDownWidth by remember { mutableStateOf(Size.Zero) }
    val iconBrand = if (expandedBrand) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconTypeBody = if (expandedTypeBody) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconTypeGear = if (expandedTypeGear) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconClass = if (expandedClass) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconColor = if (expandedColor) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    val colorByName = mapOf(
        "Красный" to Color.Red,
        "Синий" to Color.Blue,
        "Серый" to Color.Gray,
        "Желтый" to Color.Yellow,
        "Оранжевый" to Color(0xFFffa114)
    )



    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Редактирование авто",
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColorLight
            )
        )
        Row(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
        ){

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Brand auto
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedBrand.value,
                        onValueChange = {
                            selectedBrand.value = it
                        },
                        readOnly = true,
                        label = { Text("Бренд") },
                        trailingIcon = {
                            Icon(iconBrand, null, Modifier.clickable { expandedBrand = !expandedBrand })
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .onGloballyPositioned { coordinates ->
                                dropDownWidth = coordinates.size.toSize()
                            },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = expandedBrand,
                        onDismissRequest = { expandedBrand = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                    ) {
                        brands.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedBrand = !expandedBrand
                                    selectedBrand.value = label
                                }
                            ) {
                                Text(text = label)
                            }
                        }
                    }
                }

                // Model auto
                OutlinedTextField(
                    modifier = Modifier.width(300.dp),
                    value = textModel.value,
                    onValueChange = { textModel.value = it },
                    label = { Text("Модель") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { textModel.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (textModel.value.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Class auto
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedClass.value,
                        onValueChange = { selectedClass.value = it },
                        readOnly = true,
                        label = { Text("Класс") },
                        trailingIcon = {
                            Icon(iconClass, null, Modifier.clickable { expandedClass = !expandedClass })
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .onGloballyPositioned { coordinates ->
                                dropDownWidth = coordinates.size.toSize()
                            },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = expandedClass,
                        onDismissRequest = { expandedClass = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                    ) {
                        typeClass.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedClass = !expandedClass
                                    selectedClass.value = label
                                }
                            ) {
                                Text(text = label)
                            }
                        }
                    }
                }

                // Price Auto
                OutlinedTextField(
                    modifier = Modifier.width(300.dp),
                    value = textPrice.value,
                    onValueChange = {it ->
                        textPrice.value = it.filter { it.isDigit() }
                    },
                    label = { Text("Цена") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { textPrice.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (textPrice.value.isNotEmpty()) iconFieldColor else Color.Transparent
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Row(
                modifier = Modifier.weight(1f)
            ) {

                // Type body auto
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedTypeBody.value,
                        onValueChange = { selectedTypeBody.value = it },
                        readOnly = true,
                        label = { Text("Тип кузова") },
                        trailingIcon = {
                            Icon(iconTypeBody, null, Modifier.clickable { expandedTypeBody = !expandedTypeBody })
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .onGloballyPositioned { coordinates ->
                                dropDownWidth = coordinates.size.toSize()
                            },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = expandedTypeBody,
                        onDismissRequest = { expandedTypeBody = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                    ) {
                        typesBody.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedTypeBody = !expandedTypeBody
                                    selectedTypeBody.value = label
                                }
                            ) {
                                Text(text = label)
                            }
                        }
                    }
                }

                // Type gear auto
                Box {
                    OutlinedTextField(
                        value = selectedTypeGear.value,
                        onValueChange = { selectedTypeGear.value = it },
                        readOnly = true,
                        label = { Text("Тип КПП") },
                        trailingIcon = {
                            Icon(iconTypeGear, null, Modifier.clickable { expandedTypeGear = !expandedTypeGear })
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .onGloballyPositioned { coordinates ->
                                dropDownWidth = coordinates.size.toSize()
                            },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = expandedTypeGear,
                        onDismissRequest = { expandedTypeGear = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                    ) {
                        typesGear.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedTypeGear = !expandedTypeGear
                                    selectedTypeGear.value = label
                                }
                            ) {
                                Text(text = label)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {

                // color auto
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedColor.value,
                        onValueChange = { selectedColor.value = it },
                        readOnly = true,
                        label = { Text("Цвет авто") },
                        trailingIcon = {
                            Icon(iconColor, null, Modifier.clickable { expandedColor = !expandedColor })
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .onGloballyPositioned { coordinates ->
                                dropDownWidth = coordinates.size.toSize()
                            },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = expandedColor,
                        onDismissRequest = { expandedColor = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                    ) {
                        colors.forEach { color ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedColor = !expandedColor
                                    selectedColor.value = color
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(colorByName[color]!!)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(.4f).padding(top = 24.dp)
        ) {
            SliderWithLabel(
                sliderPosition.value,
                (1.0f..10.0f),
                false,
                24.dp,
                onRadiusChange = { sliderPosition.value = it.toFloat() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val newCar = Car(
                            1,
                            selectedBrand.value,
                            textModel.value,
                            selectedTypeBody.value,
                            selectedTypeGear.value,
                            sliderPosition.value.toInt(),
                            selectedClass.value,
                            textPrice.value.toFloat(),
                            selectedColor.value
                        )
                        scope.launch {
                            colorSnackBar.value = primaryColor
                            when (scaffoldState.snackbarHostState.showSnackbar("Добавить ?", "Да")) {
                                SnackbarResult.ActionPerformed -> {
                                    cars.add(newCar)
                                    launch {
                                        colorSnackBar.value = successColor
                                        scaffoldState.snackbarHostState.showSnackbar("Добавлено!")
                                    }
                                }
                                SnackbarResult.Dismissed -> {}
                            }
                        }
                    },
                    contentPadding = PaddingValues(16.dp),
                    shape = RoundedCornerShape(10.dp)
                ){
                    Text("Добавить", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {

                        scope.launch {
                            colorSnackBar.value = primaryColor
                            when (scaffoldState.snackbarHostState.showSnackbar("Удалить ?", "Да")) {
                                SnackbarResult.ActionPerformed -> {
                                    cars.removeAt(indexCar.value)
                                    launch {
                                        colorSnackBar.value = successColor
                                        scaffoldState.snackbarHostState.showSnackbar("Удалено!")
                                    }
                                }
                                SnackbarResult.Dismissed -> {}
                            }
                        }
                    },
                    contentPadding = PaddingValues(16.dp),
                    shape = RoundedCornerShape(10.dp)
                ){
                    Text("Удалить", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        val newCar = Car(
                            1,
                            selectedBrand.value,
                            textModel.value,
                            selectedTypeBody.value,
                            selectedTypeGear.value,
                            sliderPosition.value.toInt(),
                            selectedClass.value,
                            textPrice.value.toFloat(),
                            selectedColor.value
                        )

                        scope.launch {
                            colorSnackBar.value = primaryColor
                            when (scaffoldState.snackbarHostState.showSnackbar("Изменить ?", "Да")) {
                                SnackbarResult.ActionPerformed -> {
                                    cars[indexCar.value] = newCar
                                    launch {
                                        colorSnackBar.value = successColor
                                        scaffoldState.snackbarHostState.showSnackbar("Изменено!")
                                    }
                                }
                                SnackbarResult.Dismissed -> {}
                            }
                        }
                    },
                    contentPadding = PaddingValues(16.dp),
                    shape = RoundedCornerShape(10.dp)
                ){
                    Text("Изменить", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SliderWithLabel(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    finiteEnd: Boolean,
    labelMinWidth: Dp = 24.dp,
    onRadiusChange: (String) -> Unit
) {
    Column {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {

            val offset = getSliderOffset(
                value = value,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp
            )

            val endValueText =
                if (!finiteEnd && value >= valueRange.endInclusive) "${
                    value.toInt()
                }" else value.toInt().toString()


            Text(
                "Кол-во мест:",
                style = TextStyle(
                    color = textColor,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (value > valueRange.start) {
                SliderLabel(
                    label = endValueText, minWidth = labelMinWidth, modifier = Modifier.padding(start = offset)
                )
            }
        }
        Slider(
            value = value,
            onValueChange = {
                onRadiusChange(it.toString())
            },
            onValueChangeFinished = { },
            valueRange = valueRange,
            steps = 10,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SliderLabel(
    label: String,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        label,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .background(
                color = primaryColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}

private fun getSliderOffset(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    boxWidth: Dp,
    labelWidth: Dp
): Dp {

    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

    return (boxWidth - labelWidth) * positionFraction
}

private fun calcFraction(
    a: Float, b: Float, pos: Float
) = (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)
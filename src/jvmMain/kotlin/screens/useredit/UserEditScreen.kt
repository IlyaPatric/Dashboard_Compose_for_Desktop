package screens.useredit

/*
*  Описание экрана Администрирования
* */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import data.remote.models.User
import kotlinx.coroutines.launch
import styles.*
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@Composable
fun UserEditScreen(
    listState: LazyListState
) {

    val headerString = listOf("Фамилия", "Имя", "Отчество", "Должность", "Роль")

    val usersList = remember { mutableStateListOf(
        User(1,"Патурин", "Илья", "Дмитриевич", "Программист", "Админ"),
        User(2,"Ежов", "Константин", "Артёмович", "Менеджер", "Обычный"),
        User(3,"Сазонов", "Иван", "Александрович", "Программист", "Админ"),
        User(4,"Коновалов", "Даниил", "Артурович", "Менеджер", "Обычный"),
        User(5,"Смирнов", "Александр", "Павлович", "П. менеджер", "Обычный"),
        User(6,"Никитин", "Кирилл", "Фёдорович", "Менеджер по О.К", "Обычный"),
        User(7,"Иванова", "Вероника", "Арсентьевна", "Менеджер", "Обычный"),
        User(8,"Агафонова", "Ева", "Марковна", "Программист", "Админ"),
        User(9,"Матвеева", "Мария", "Львовна", "Менеджер", "Обычный"),
        User(10,"Полякова", "Варвара", "Степановна", "Менеджер по О.К", "Обычный"),
        User(11,"Голованова", "Ева", "Львовна", "Менеджер", "Админ")
    ) }


    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var isOpenDel by remember { mutableStateOf(false) }
    var isOpenUpd by remember { mutableStateOf(false) }
    var indexUser by remember { mutableStateOf(0) }

    var fName by remember { mutableStateOf("") }
    var sName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }

    var selectedPost by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }

    var expandedPost by remember { mutableStateOf(false) }
    var expandedRole by remember { mutableStateOf(false) }

    val post = listOf("Программист", "Менеджер", "П. менеджер", "Менеджер по О.К")
    val role = listOf("Админ", "Обычный")

    var dropDownWidth by remember { mutableStateOf(Size.Zero) }
    val iconPost = if (expandedPost) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val iconRole = if (expandedRole) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

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

        Box(
            Modifier.fillMaxSize().background(Color(0xFFDCD3EE).copy(0.2f))
        ){
            Column(Modifier.fillMaxSize().background(Color(0xFFDCD3EE).copy(0.2f))) {
                Card(
                    elevation = 2.dp,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row (
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(2f)){
                            Text(
                                "",
                                style = TextStyle(
                                    color = MaterialTheme.colors.primary,
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        headerString.forEach {header ->

                            Column(modifier = Modifier.weight(1f)){
                                Text(
                                    header,
                                    style = TextStyle(
                                        color = MaterialTheme.colors.primary,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
                Box {
                    LazyColumn(
                        state = listState
                    ){
                        itemsIndexed(usersList){index, user ->
                            val swipeableState = rememberSwipeableState(initialValue = 0)
                            val sizePx = with(LocalDensity.current) { 48.dp.toPx() }
                            val anchors = mapOf(0f to 0, sizePx * 4 to 1)

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

                            ){
                                Row(Modifier.padding(start = 24.dp).align(Alignment.CenterStart)) {
                                    // изменить
                                    IconButton(
                                        onClick = {
                                            isOpenUpd = true
                                            indexUser = index
                                            fName = user.fName
                                            sName = user.sName
                                            lName = user.lName
                                            selectedPost = user.post
                                            selectedRole = user.role
                                        },
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
                                    Spacer(Modifier.width(24.dp))
                                    // удалить
                                    IconButton(
                                        onClick = {
                                            isOpenDel = true
                                            indexUser = index
                                        },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF4D4D).copy(0.1f))
                                    ){
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null,
                                            tint = Color(0xFFFF4D4D)
                                        )
                                    }
                                }

                                Card(
                                    modifier = Modifier
                                        .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                                        .fillMaxWidth()
                                ){
                                    Row(
                                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp).fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Column(
                                            modifier = Modifier.weight(2f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.AccountCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colors.primary
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(user.fName)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(user.sName)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(user.lName)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(user.post)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = user.role,
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .background(if (user.role == "Админ") successColor.copy(0.5f) else errorColor.copy(0.5f))
                                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = listState
                        )
                    )
                }

            }

            // для удаления
            AnimatedVisibility(
                visible = isOpenDel,
                enter = expandVertically(),
                exit = shrinkVertically(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, primaryColor, RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Удалить пользователя?",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColorLight
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            Button(onClick = {
                                usersList.removeAt(indexUser)
                                isOpenDel = false
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Успешно удалено!")
                                }
                            }) {
                                Text("Да", fontWeight = FontWeight.Bold)
                            }
                            Button(onClick = {
                                isOpenDel = false
                            }) {
                                Text("Нет", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // для редактирования
            AnimatedVisibility(
                visible = isOpenUpd,
                enter = expandVertically(),
                exit = shrinkVertically(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, primaryColor, RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                    ){

                        Text(
                            text = "Изменение пользователя",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColorLight
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = fName,
                                onValueChange = { fName = it },
                                label = { Text("Фамилия") },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { fName = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (fName.isNotEmpty()) iconFieldColor else Color.Transparent
                                        )
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = sName,
                                onValueChange = { sName = it },
                                label = { Text("Имя") },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { sName = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (sName.isNotEmpty()) iconFieldColor else Color.Transparent
                                        )
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = lName,
                                onValueChange = { lName = it },
                                label = { Text("Отчество") },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { lName = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (lName.isNotEmpty()) iconFieldColor else Color.Transparent
                                        )
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 16.dp)
                        ){
                            Box {
                                OutlinedTextField(
                                    value = selectedPost,
                                    onValueChange = { selectedPost = it },
                                    readOnly = true,
                                    label = { Text("Должность") },
                                    trailingIcon = {
                                        Icon(iconPost, null, Modifier.clickable { expandedPost = !expandedPost })
                                    },
                                    modifier = Modifier
                                        .onGloballyPositioned { coordinates ->
                                            dropDownWidth = coordinates.size.toSize()
                                        },
                                    shape = RoundedCornerShape(10.dp)
                                )

                                DropdownMenu(
                                    expanded = expandedPost,
                                    onDismissRequest = { expandedPost = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                                        .height(100.dp)
                                ) {
                                    post.forEach { label ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expandedPost = !expandedPost
                                                selectedPost = label
                                            }
                                        ) {
                                            Text(text = label)
                                        }
                                    }
                                }
                            }
                            Box {
                                OutlinedTextField(
                                    value = selectedRole,
                                    onValueChange = { selectedRole = it },
                                    readOnly = true,
                                    label = { Text("Роль") },
                                    trailingIcon = {
                                        Icon(iconRole, null, Modifier.clickable { expandedRole = !expandedRole })
                                    },
                                    modifier = Modifier
                                        .onGloballyPositioned { coordinates ->
                                            dropDownWidth = coordinates.size.toSize()
                                        },
                                    shape = RoundedCornerShape(10.dp)
                                )

                                DropdownMenu(
                                    expanded = expandedRole,
                                    onDismissRequest = { expandedRole = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { dropDownWidth.width.toDp() })
                                        .height(100.dp)
                                ) {
                                    role.forEach { label ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expandedRole = !expandedRole
                                                selectedRole = label
                                            }
                                        ) {
                                            Text(text = label)
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ){
                            Button(onClick = {
                                    val user = User(
                                        indexUser,
                                        fName,
                                        sName,
                                        lName,
                                        selectedPost,
                                        selectedRole
                                    )
                                    usersList[indexUser] = user
                                    isOpenUpd = false
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("Успешно изменено!")
                                    }
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Изменить", fontWeight = FontWeight.Bold)
                            }
                            Button(onClick = {
                                isOpenUpd = false
                            }) {
                                Text("Не_надо", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
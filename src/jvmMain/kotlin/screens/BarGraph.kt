package screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import styles.primaryColor
import styles.textColor
import kotlin.math.round

@ExperimentalFoundationApi
@Composable
fun BarGraph(
    graphBarData: List<Float>,
    xAxisScaleData: List<String>,
    barData_: List<Int>,
    height: Dp,
    barWidth: Dp,
    barColor: Color,
    barArrangement: Arrangement.Horizontal
){
    val barData by remember {
        mutableStateOf(barData_ + 0)
    }

    val width = 800.dp

    val xAxisScaleHeight = 25.dp

    val yAxisScalePadding by remember {
        mutableStateOf(25f)
    }
    val yAxisTextWidth by remember {
        mutableStateOf(10.dp)
    }

    // bar shape
    val barShape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)

    val yCoordinates = mutableListOf<Float>()

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f),0f)


    Box(
       modifier = Modifier.fillMaxWidth(),
       contentAlignment = Alignment.TopStart
    ){

        // horizontal dotted lines on graph
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 10.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ){
                val yAxisScaleText = barData.max() / 3f
                (0..3).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawString(
                            round(barData.min() + yAxisScaleText*i).toString(),
                            30f,
                            size.height - yAxisScalePadding - i * size.height / 3f,
                            Font(),
                            Paint()
                        )
                    }
                    yCoordinates.add(size.height - yAxisScalePadding - i * size.height / 3f)
                }

                (1..3).forEach {
                    drawLine(
                        start = Offset(x = yAxisScalePadding + 30f, y = yCoordinates[it]),
                        end = Offset(x = size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 2f,
                        pathEffect = pathEffect
                    )
                }
            }
        }


        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ){

            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ){

               graphBarData.forEachIndexed {index, value ->

                   var animationTriggered by remember {
                       mutableStateOf(false)
                   }
                   val graphBarHeight by animateFloatAsState(
                       targetValue = if (animationTriggered) value else 0f,
                       animationSpec = tween(
                           durationMillis = 1000,
                           delayMillis = 0
                       )
                   )
                   LaunchedEffect(key1 = true){
                       animationTriggered = true
                   }

                   Column(
                       modifier = Modifier
                           .fillMaxHeight()
                           .padding(end = 12.dp),
                       verticalArrangement = Arrangement.Top,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ){

                       // Each graph
                       Box(
                           modifier = Modifier
                               .padding(bottom = 5.dp)
                               .clip(barShape)
                               .width(barWidth)
                               .height(height - 10.dp)
                               .background(Color.Transparent),
                           contentAlignment = Alignment.BottomCenter
                       ) {

                            TooltipArea(
                                tooltip = {
                                    Surface(
                                        modifier = Modifier.shadow(2.dp),
                                        color = Color.White,
                                        contentColor = primaryColor,
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, primaryColor)
                                    ) {
                                        Text(
                                            text = "${(value * 100).toInt()}k ₽",
                                            modifier = Modifier.padding(10.dp),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            ){
                               Box(
                                   modifier = Modifier
                                       .clip(barShape)
                                       .fillMaxWidth()
                                       .fillMaxHeight(graphBarHeight)
                                       .background(barColor)
                               )
                            }
                       }

                       Column(
                           modifier = Modifier
                               .height(xAxisScaleHeight),
                           verticalArrangement = Arrangement.Top,
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {

                           Text(
                               modifier = Modifier.padding(bottom = 3.dp),
                               text = xAxisScaleData[index].toString(),
                               fontSize = 14.sp,
                               fontWeight = FontWeight.Medium,
                               textAlign = TextAlign.Center,
                               color = textColor
                           )
                       }
                   }
               }
            }
        }
    }
}















import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition


@Composable
@Preview
fun App(
    n: MutableState<Int>,
    listRandom: MutableState<List<Float>>,
    listBar: MutableState<List<List<Double>>>
) {
    MaterialTheme {
        Row {
            Column(Modifier.padding(16.dp).weight(2f)) {
                buttons(n, listRandom, listBar)
                data(n, listRandom, listBar)
            }
            Column(Modifier.padding(16.dp).weight(5f)){
                chart(listBar)
            }
        }
    }
}

@Composable
fun buttons(
    n: MutableState<Int>,
    listRandom: MutableState<List<Float>>,
    listBar: MutableState<List<List<Double>>>
){
    Column {
        Button(
            onClick = {
                n.value = 50
                listRandom.value = generation(n.value)
                listBar.value = separation(n.value, listRandom.value)
                },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("50")
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Button(
            onClick = { n.value = 100
                listRandom.value = generation(n.value)
                listBar.value = separation(n.value, listRandom.value)
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("100")
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Button(
            onClick = { n.value = 1000
                listRandom.value = generation(n.value)
                listBar.value = separation(n.value, listRandom.value)},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("1000")
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Button(
            onClick = { n.value = 100000
                listRandom.value = generation(n.value)
                listBar.value = separation(n.value, listRandom.value)},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("100000")
        }
    }
}

@Composable
fun chart(listBar: MutableState<List<List<Double>>>){
    if(listBar.value[0].isNotEmpty() && listBar.value[1].isNotEmpty()) {
        val listX = mutableListOf<String>()
        for(i in 0..listBar.value[0].size - 1){
            listX.add(
                listBar.value[0][i].toString().take(5)
            )
        }

        val testBarParameters: List<BarParameters> = listOf(
            BarParameters(
                dataName = "Экспериментальная выборка      ",
                data = listBar.value[1],
                barColor = Color.Blue
            ),
            BarParameters(
                dataName = "Теоретическая выборка",
                data = listBar.value[2],
                barColor = Color.Red
            )
        )

        Box(Modifier.wrapContentWidth().wrapContentHeight()) {
            BarChart(
                chartParameters = testBarParameters,
                gridColor = Color.DarkGray,
                horizontalArrangement = Arrangement.Center,
                xAxisData = listX,
                isShowGrid = true,
                animateChart = true,
                showGridWithSpacer = false,
                yAxisStyle = TextStyle(
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 8.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.W400
                ),
                barWidth = 8.dp,
                spaceBetweenBars = 4.dp,
                spaceBetweenGroups = 20.dp,
                legendPosition = LegendPosition.BOTTOM
            )
        }
    }
}

@Composable
fun data(
    n: MutableState<Int>,
    listRandom: MutableState<List<Float>>,
    listBar: MutableState<List<List<Double>>>
){
    if(n.value != 0) {
        val listData = calculation(n.value, listRandom.value)
        Text(text = "Объем выборки: ${n.value}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Теоретическое математическое ожидание: 1", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Экспериментальное математическое ожидание: ${listData[0]}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Теоретическая дисперсия: 0.1666666", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Экспериментальная дисперсия: ${listData[1]}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Минимальное значение: ${listRandom.value.first()}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Максимальное значение: ${listRandom.value.last()}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Критерий Пирсона: ${listBar.value[3].first()}", fontSize = 12.sp)
        Spacer(modifier = Modifier.padding(4.dp))
        if(listBar.value[3].last() == 0.0) {
            Text(text = "Ошибка", fontSize = 12.sp)
        } else if(listBar.value[3].last() == 1.0) {
            Text(text = "Критерий не выполняется", fontSize = 12.sp)
        } else if(listBar.value[3].last() == 2.0) {
            Text(text = "Критерий не выполняется. Нужно увеличить выборку.", fontSize = 12.sp)
        } else {
            Text(text = "Критерий выполняется", fontSize = 12.sp)
        }
    }
}

fun main() = application {

    val n = mutableStateOf(0)
    val listRandom: MutableState<List<Float>> = mutableStateOf(listOf())
    val listBar: MutableState<List<List<Double>>> = mutableStateOf(listOf(listOf()))

    Window(onCloseRequest = ::exitApplication) {
        App(n, listRandom, listBar)
    }
}

package kz.vasilyev.agrotechapp.feature.analytics

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kz.vasilyev.agrotechapp.data.RoomInstance
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary
import java.util.concurrent.TimeUnit

@Composable
fun AnalyticsScreen(innerPadding: PaddingValues, navController: NavController, gardenId: Long) {
    val context = LocalContext.current
    
    val notes = remember {
        RoomInstance
            .getInstance(context.applicationContext as Application)
            .roomDao()
            .getNotesForGarden(gardenId)
            .sortedBy { it.createdAt }
    }

    // Преобразуем данные для графиков
    val growthData = remember(notes) {
        if (notes.isEmpty()) entryModelOf(0f to 0f)
        else entryModelOf(*notes.mapIndexed { index, note ->
            index.toFloat() to note.height.toFloat()
        }.toTypedArray())
    }

    val waterTempData = remember(notes) {
        if (notes.isEmpty()) entryModelOf(0f to 0f)
        else entryModelOf(*notes.mapIndexed { index, note ->
            index.toFloat() to note.waterTemp.toFloat()
        }.toTypedArray())
    }

    val soilHumidityData = remember(notes) {
        if (notes.isEmpty()) entryModelOf(0f to 0f)
        else entryModelOf(*notes.mapIndexed { index, note ->
            index.toFloat() to note.humidity.toFloat()
        }.toTypedArray())
    }

    val lightData = remember(notes) {
        if (notes.isEmpty()) entryModelOf(0f to 0f)
        else entryModelOf(*notes.mapIndexed { index, note ->
            index.toFloat() to note.light.toFloat()
        }.toTypedArray())
    }

    Scaffold(
        containerColor = BackgroundScreen,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад"
                    )
                }

                Text(
                    text = "Аналитика",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPaddings ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddings),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет данных для анализа",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPaddings)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Общая статистика
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        "Общая статистика",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    val totalDays = TimeUnit.MILLISECONDS.toDays(
                        notes.last().createdAt - notes.first().createdAt
                    ).toInt()
                    
                    Text("Дней наблюдения: $totalDays")
                    Text("Всего записей: ${notes.size}")
                    Text("Средняя высота: ${notes.map { it.height }.average().toInt()} мм")
                    Text("Средняя влажность: ${notes.map { it.humidity }.average().toInt()}%")
                }

                Text("Рост растения (мм)", fontWeight = FontWeight.SemiBold)
                MyLineChart(data = growthData)

                Text("Температура воды (°C)", fontWeight = FontWeight.SemiBold)
                MyLineChart(data = waterTempData)

                Text("Влажность почвы (%)", fontWeight = FontWeight.SemiBold)
                MyLineChart(data = soilHumidityData)

                Text("Освещённость (lux)", fontWeight = FontWeight.SemiBold)
                MyLineChart(data = lightData)
            }
        }
    }
}

@Composable
fun MyLineChart(data: ChartEntryModel) {
    val primaryColor = Primary

    Chart(
        chart = lineChart(
            lines = listOf(
                lineSpec(
                    lineColor = primaryColor,
                    pointConnector = DefaultPointConnector(cubicStrength = 0.2f),
                    point = shapeComponent(shape = CircleShape, color = primaryColor),
                    pointSize = 6.dp,
                    lineThickness = 3.dp
                )
            )
        ),
        model = data,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(),
        marker = customPrimaryMarker(),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
    )
}

@Composable
fun customPrimaryMarker(): MarkerComponent {
    val primaryColor = MaterialTheme.colorScheme.primary

    val label = textComponent(
        color = Color.White,
        background = shapeComponent(
            shape = RoundedCornerShape(6.dp),
            color = primaryColor
        ),
    )

    val indicator = shapeComponent(
        shape = CircleShape,
        color = primaryColor
    )

    val guideline = lineComponent(
        color = primaryColor.copy(alpha = 0.5f),
        thickness = 2.dp
    )

    return remember {
        MarkerComponent(
            label = label,
            indicator = indicator,
            guideline = guideline
        )
    }
}





package kz.vasilyev.agrotechapp.feature.analytics

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
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import com.patrykandpatrick.vico.core.dimensions.Dimensions
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary

@Composable
fun AnalyticsScreen(innerPadding: PaddingValues, navController: NavController) {
    val growthData = remember {
        entryModelOf(
            0f to 20f, 1f to 35f, 2f to 50f, 3f to 68f, 4f to 75f,
            5f to 90f, 6f to 108f, 7f to 125f, 8f to 138f, 9f to 150f
        )
    }

    val waterTempData = remember {
        entryModelOf(
            0f to 22.5f, 1f to 23.0f, 2f to 22.7f, 3f to 23.4f, 4f to 23.1f,
            5f to 22.9f, 6f to 23.6f, 7f to 23.8f, 8f to 23.2f, 9f to 23.0f
        )
    }

    val soilHumidityData = remember {
        entryModelOf(
            0f to 60f, 1f to 58f, 2f to 55f, 3f to 52f, 4f to 50f,
            5f to 49f, 6f to 47f, 7f to 46f, 8f to 48f, 9f to 51f
        )
    }

    val lightData = remember {
        entryModelOf(
            0f to 800f, 1f to 1000f, 2f to 950f, 3f to 1100f, 4f to 980f,
            5f to 1020f, 6f to 1080f, 7f to 1050f, 8f to 1000f, 9f to 980f
        )
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
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
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





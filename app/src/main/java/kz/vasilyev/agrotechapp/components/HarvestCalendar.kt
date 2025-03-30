package kz.vasilyev.agrotechapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kz.vasilyev.agrotechapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun HarvestCalendar(
    harvestDates: List<LocalDate>,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val dates = (-15..15).map { today.plusDays(it.toLong()) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            listState.scrollToItem(15) // Индекс текущей даты (середина списка)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Календарь сбора",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF88C057)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dates.size) { index ->
                    val date = dates[index]
                    val isHarvestDay = harvestDates.any { 
                        ChronoUnit.DAYS.between(it, date) == 0L 
                    }
                    
                    CalendarDay(
                        date = date,
                        isHarvestDay = isHarvestDay,
                        isToday = date == today
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isHarvestDay: Boolean,
    isToday: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(36.dp)
    ) {
        if (isHarvestDay) {
            Icon(
                painter = painterResource(R.drawable.ic_harvest),
                contentDescription = null,
                tint = Color(0xFF88C057),
                modifier = Modifier.size(16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Text(
            text = date.format(DateTimeFormatter.ofPattern("dd")),
            fontSize = 14.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            modifier = if (isToday) {
                Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF88C057))
                    .padding(3.dp)
            } else {
                Modifier.padding(3.dp)
            },
            color = if (isToday) Color.White else Color.Black
        )
        
        Text(
            text = date.format(DateTimeFormatter.ofPattern("MM")),
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
} 
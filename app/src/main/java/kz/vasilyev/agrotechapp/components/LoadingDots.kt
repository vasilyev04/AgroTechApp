package kz.vasilyev.agrotechapp.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFA9D66A)
) {
    val dots = listOf(".", ".", ".")
    val transition = rememberInfiniteTransition(label = "dots")
    val alphas = dots.mapIndexed { index, _ ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0f at (index * 333)
                    1f at (index * 333 + 166)
                    0f at (index * 333 + 333)
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "dot$index"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Печатает",
            fontSize = 14.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
        dots.forEachIndexed { index, dot ->
            Text(
                text = dot,
                fontSize = 14.sp,
                color = color.copy(alpha = alphas[index].value),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 1.dp)
            )
        }
    }
} 
package kz.vasilyev.agrotechapp.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BackgroundScreen = Color(0xFFF4F4F4)
val Primary = Color(0xFF99C951)
val Secondary = Color(0xFFD6FAA0)

val SearchBarColors: TextFieldColors
    @Composable
    get() = TextFieldDefaults.colors(
        //Background
        disabledContainerColor = White,
        unfocusedContainerColor = White,
        focusedContainerColor = White,

        //Stroke Line
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,

        //Start Icon
        unfocusedLeadingIconColor = White,
        focusedLeadingIconColor = White,

        //Hint
        unfocusedPlaceholderColor = White,
        focusedPlaceholderColor = White,

        //End Icon
        unfocusedTrailingIconColor = White,
        focusedTrailingIconColor = White,
        disabledTrailingIconColor = White,
        cursorColor = Primary
    )
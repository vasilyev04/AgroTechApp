package kz.vasilyev.agrotechapp.feature.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kz.vasilyev.agrotechapp.ui.theme.Primary
import kz.vasilyev.agrotechapp.ui.theme.SearchBarColors

@Composable
fun MySearchBar(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    onSearchBarClickValueChange: () -> Unit,
){

    val source = remember<MutableInteractionSource> { MutableInteractionSource() }
    if (source.collectIsPressedAsState().value) onSearchBarClickValueChange()

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        placeholder = {
            Text(
                text = "Поиск",
                color = Primary
            )
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = Primary
            )
        },
        trailingIcon = {
            if (textFieldValue.isNotEmpty()) {
                IconButton(onClick = {
                    onTextFieldValueChange("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = Primary
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        onValueChange = onTextFieldValueChange,
        shape = RoundedCornerShape(10.dp),
        colors = SearchBarColors
    )
}
package `in`.fom.simplexui.ui.view

import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import `in`.fom.simplexui.utils.Defaults
import `in`.fom.simplexui.utils.nextAddSign
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Term(sign: String, value: String, index: Int? = null, onTap: () -> Unit, onEdit: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AlgebraSign(sign, onTap)
        StringField(value, onEdit)
        index?.let { VariableX(index = it) }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun VariableX(index: Int) {
    Text("X$index", color = MaterialTheme.colors.primarySurface)
}

@Composable
fun StringField(value: String, onEdit: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onEdit,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .defaultMinSize(minWidth = 8.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Composable
fun AlgebraSign(value: String, onTap: () -> Unit) {
    Box(modifier = Modifier
        .height(IntrinsicSize.Min)
        .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
        .defaultMinSize(40.dp, 40.dp)
        .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value, color = MaterialTheme.colors.background, modifier = Modifier.clickable { onTap() })
    }
}

// Preview for test
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimplexUiTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = MaterialTheme.colors.background
        ) {
            var mutableText by remember { mutableStateOf(Defaults.WEIGHT.toString()) }
            var mutableSign by remember { mutableStateOf(Defaults.defaultAddSign) }
            Column {
                Term(
                    index = 0,
                    value = mutableText,
                    sign = mutableSign,
                    onTap = { mutableSign = mutableSign.nextAddSign() },
                    onEdit = { newValue -> mutableText = newValue }
                )
            }
        }
    }
}


package `in`.fom.simplexui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimplexUiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FunctionView()
                }
            }
        }
    }
}

@Composable
fun FunctionView(model: MainViewModel = viewModel()) {
    val coefficients by model.mutableVector.collectAsState(emptyList())
    LazyRow {
        itemsIndexed(coefficients) { index: Int, item: Double ->
            Term(index, item) { model.setFunctionCoefficient(index, it.toDouble()) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimplexUiTheme {
    }
}

@Composable
fun Sign(signs: List<String>) {
    var index = 0
    var signState by remember { mutableStateOf(signs[index]) }
    Button(onClick = {
        index = (index + 1) % signs.size
        signState = signs[index]
    }) {
        Text(text = signState)
    }
}

@Composable
fun AlgebraSign() {
    Sign(listOf("-", "+"))
}

@Composable
fun InequalitySign() {
    Sign(listOf("<", "=", ">"))
}

@Composable
fun NumberField(value: Double, onEdit: (String) -> Unit) {
    BasicTextField(value.toString(), onEdit, Modifier.defaultMinSize(20.dp))
}

@Composable
fun VariableX(index: Int) {
    Text("X$index")
}

@Composable
fun Term(index: Int, value: Double, onEdit: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AlgebraSign()
        NumberField(value, onEdit)
        VariableX(index = index)
    }
}
package `in`.fom.simplexui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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
                    SimplexView()
                }
            }
        }
    }
}

@Composable
fun SimplexView(viewModel: MainViewModel = viewModel()) {
    Column {
        FunctionView()
        Row {
            Button(onClick = { viewModel.putArg() }) {
                Text(text = "+ arg")
            }
            Button(onClick = { viewModel.dropArg() }) {
                Text(text = "- arg")
            }
        }
    }
}

@Composable
fun FunctionView(viewModel: MainViewModel = viewModel()) {
    val coefficients by viewModel.mutableVector.collectAsState(emptyList())
    LazyRow(verticalAlignment = Alignment.CenterVertically) {
        item {
            Text(text = "f(X̂, Ĉ) =")
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
        itemsIndexed(coefficients) { index: Int, item: String ->
            Term(index, item) { viewModel.setFunctionCoefficient(index, it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimplexUiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                Term(0, "1.2") {}
                SimplexView()
            }
        }
    }
}

@Composable
fun Sign(signs: List<String>) {
    var index = 0
    var signState by remember { mutableStateOf(signs[index]) }
    Box(modifier = Modifier.height(IntrinsicSize.Min)
            .background(color = Color.Magenta, shape = CircleShape)
            .defaultMinSize(40.dp, 40.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = signState, modifier = Modifier.clickable {
                index = (index + 1) % signs.size
                signState = signs[index]
            }
        )
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
    StringField(value.toString(), onEdit)
}

@Composable
fun StringField(value: String, onEdit: (String) -> Unit) {
    BasicTextField(value, onEdit,
        Modifier
            .width(IntrinsicSize.Min)
            .defaultMinSize(minWidth = 8.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Composable
fun VariableX(index: Int) {
    Text("X$index", color = Color.Green)
}

@Composable
fun Term(index: Int, value: String, onEdit: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AlgebraSign()
        StringField(value, onEdit)
        VariableX(index = index)
        Spacer(modifier = Modifier.width(8.dp))
    }
}
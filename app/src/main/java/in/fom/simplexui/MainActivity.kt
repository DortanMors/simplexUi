package `in`.fom.simplexui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import `in`.fom.simplexui.utils.Defaults.defaultBoundsMatrix
import `in`.fom.simplexui.utils.Defaults.defaultBoundsSigns
import `in`.fom.simplexui.utils.Defaults.defaultBoundsVector
import `in`.fom.simplexui.utils.Defaults.defaultFunctionSigns
import `in`.fom.simplexui.utils.Defaults.defaultFunctionWeights
import `in`.fom.simplexui.utils.Defaults.defaultInequalitySigns
import `in`.fom.simplexui.utils.Defaults.defaultMatrixSigns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "f(X̂, Ĉ) =")
            Spacer(modifier = Modifier.width(8.dp))
            TermLineView(viewModel)
        }
        Row {
            Button(onClick = { viewModel.putArg() }) {
                Text(text = "+ arg")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.dropArg() }) {
                Text(text = "- arg")
            }
        }
        InequalitiesView(viewModel)
    }
}

@Composable
fun InequalitiesView(viewModel: MainViewModel = viewModel()) {
    Column {
        Row {
            Button(onClick = { viewModel.putInequalityRow() }) {
                Text(text = "+ inequality")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.dropInequalityRow() }) {
                Text(text = "- inequality")
            }
        }
        BoundsMatrixView(viewModel)
    }
}

@Composable
fun BoundsMatrixView(viewModel: MainViewModel = viewModel()) {
    val matrix by viewModel.boundsMatrix.collectAsState(defaultBoundsMatrix)
    val matrixSigns by viewModel.matrixSigns.collectAsState(defaultMatrixSigns)
    val signs by viewModel.inequalitySigns.collectAsState(defaultInequalitySigns)
    val bounds by viewModel.boundsVector.collectAsState(defaultBoundsVector)
    val boundsSigns by viewModel.boundsSigns.collectAsState(defaultBoundsSigns)
    LazyColumn {
        itemsIndexed(matrix) { rowIndex, line ->
            LazyRow {
                itemsIndexed(line) { columnIndex, boundWeight ->
                    Term(
                        index = columnIndex,
                        sign = matrixSigns[rowIndex][columnIndex],
                        value = boundWeight,
                        onTap = { viewModel.switchMatrixSign(rowIndex, columnIndex) },
                        onEdit = { newValue -> viewModel.setBoundsWeight(rowIndex, columnIndex, newValue) }
                    )
                }
                item {
                    AlgebraSign(value = signs[rowIndex]) { viewModel.switchInequalitySign(rowIndex) }
                }
                item {
                    Term(
                        sign = boundsSigns[rowIndex],
                        value = bounds[rowIndex],
                        onTap = { viewModel.switchBoundsSign(rowIndex) },
                        onEdit = { newValue -> viewModel.setBound(rowIndex, newValue) }
                    )
                }
            }
        }
    }
}

@Composable
fun TermLineView(viewModel: MainViewModel = viewModel()) {
    val coefficients by viewModel.vectorWeights.collectAsState(defaultFunctionWeights)
    val signs by viewModel.functionSigns.collectAsState(defaultFunctionSigns)
    LazyRow(verticalAlignment = Alignment.CenterVertically) {
        itemsIndexed(coefficients) { i: Int, item: String ->
            Term(
                index = i,
                value = item,
                sign = signs[i],
                onTap = { viewModel.switchFunctionSign(i) },
                onEdit = { newValue -> viewModel.setWeight(i, newValue) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    SimplexUiTheme {
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            color = MaterialTheme.colors.background
//        ) {
//            Column {
//                Term(0, "1.2") {}
//                SimplexView()
//            }
//        }
//    }
}

@Composable
fun AlgebraSign(value: String, onTap: () -> Unit) {
    Box(modifier = Modifier
        .height(IntrinsicSize.Min)
        .background(color = Color.Magenta, shape = CircleShape)
        .defaultMinSize(40.dp, 40.dp)
        .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value, modifier = Modifier.clickable { onTap() })
    }
}

@Composable
fun StringField(value: String, onEdit: (String) -> Unit) {
    BasicTextField(
        value,
        onEdit,
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
fun Term(sign: String, value: String, index: Int? = null, onTap: () -> Unit, onEdit: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AlgebraSign(sign, onTap)
        StringField(value, onEdit)
        index?.let { VariableX(index = it) }
        Spacer(modifier = Modifier.width(8.dp))
    }
}
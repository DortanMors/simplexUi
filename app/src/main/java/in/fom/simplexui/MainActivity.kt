package `in`.fom.simplexui

import `in`.fom.simplexui.model.InequalityRowModel
import `in`.fom.simplexui.model.TermModel
import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import `in`.fom.simplexui.ui.view.AlgebraSign
import `in`.fom.simplexui.ui.view.Term
import `in`.fom.simplexui.utils.Defaults.defaultFunctionTerms
import `in`.fom.simplexui.utils.Defaults.defaultInequalities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimplexUiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    SimplexView()
                }
            }
        }
    }
}

@Composable
fun SimplexView(viewModel: MainViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.function))
            Spacer(modifier = Modifier.width(8.dp))
            TermLineView(viewModel)
        }
        Row {
            Button(onClick = { viewModel.putArg() }) {
                Text(text = stringResource(R.string.plus_arg))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.putInequalityRow() }) {
                Text(text = stringResource(R.string.plus_inequality))
            }
        }
        Row {
            Button(onClick = { viewModel.dropArg() }) {
                Text(text = stringResource(R.string.minus_arg))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.dropInequalityRow() }) {
                Text(text = stringResource(R.string.minus_inequality))
            }
        }
        BoundsMatrixView(viewModel)
    }
}

@Composable
fun BoundsMatrixView(viewModel: MainViewModel = viewModel()) {
    val matrix by viewModel.inequalities.collectAsState(defaultInequalities())
    Box {
        LazyColumn {
            itemsIndexed(matrix) { rowIndex: Int, line: InequalityRowModel ->
                LazyRow {
                    itemsIndexed(line.terms) { columnIndex, termModel ->
                        Term(
                            index = columnIndex,
                            sign = termModel.sign,
                            value = termModel.value,
                            onTap = { viewModel.switchMatrixSign(rowIndex, columnIndex) },
                            onEdit = { newValue ->
                                viewModel.setBoundsWeight(
                                    rowIndex,
                                    columnIndex,
                                    newValue
                                )
                            }
                        )
                    }
                    item {
                        AlgebraSign(value = line.sign) { viewModel.switchInequalitySign(rowIndex) }
                    }
                    item {
                        Term(
                            sign = line.boundSign,
                            value = line.bound,
                            onTap = { viewModel.switchBoundsSign(rowIndex) },
                            onEdit = { newValue -> viewModel.setBound(rowIndex, newValue) }
                        )
                    }
                }
            }
            item {
                Button(onClick = { viewModel.solve() }, Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.solve))
                }
            }
        }
    }
}

@Composable
fun TermLineView(viewModel: MainViewModel = viewModel()) {
    val coefficients by viewModel.functionTerms.collectAsState(defaultFunctionTerms())
    LazyRow(verticalAlignment = Alignment.CenterVertically) {
        itemsIndexed(coefficients) { i: Int, item: TermModel ->
            Term(
                index = i,
                value = item.value,
                sign = item.sign,
                onTap = { viewModel.switchFunctionSign(i) },
                onEdit = { newValue -> viewModel.setWeight(i, newValue) }
            )
        }
    }
}

package `in`.fom.simplexui

import `in`.fom.simplexui.model.InequalityRowModel
import `in`.fom.simplexui.model.TermModel
import `in`.fom.simplexui.utils.*
import `in`.fom.simplexui.utils.Defaults.MAX
import `in`.fom.simplexui.utils.Defaults.MIN
import `in`.fom.simplexui.utils.Defaults.MIN_INEQUALITIES
import `in`.fom.simplexui.utils.Defaults.MIN_TERMS
import `in`.fom.simplexui.utils.Defaults.defaultFunctionTerms
import `in`.fom.simplexui.utils.Defaults.defaultInequalities
import `in`.fom.simplexui.utils.Defaults.defaultLog
import `in`.fom.simplexui.utils.Defaults.defaultSolveMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssau.lib.simplex.Sign
import com.ssau.lib.simplex.Simplex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.*
import java.nio.charset.StandardCharsets

class MainViewModel: ViewModel() {

    private val mutableFunctionTerms: MutableStateFlow<MutableList<TermModel>> = MutableStateFlow(defaultFunctionTerms())
    val functionTerms: StateFlow<MutableList<TermModel>> = mutableFunctionTerms
    private val mutableInequalities: MutableStateFlow<MutableList<InequalityRowModel>> = MutableStateFlow(defaultInequalities())
    val inequalities: StateFlow<MutableList<InequalityRowModel>> = mutableInequalities
    private val mutableProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val progress: StateFlow<Boolean> = mutableProgress
    private val mutableSolveMode: MutableStateFlow<String> = MutableStateFlow(defaultSolveMode())
    val solveMode: StateFlow<String> = mutableSolveMode
    private val edited: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val mutableLog: MutableStateFlow<String> = MutableStateFlow(defaultLog())
    val log: StateFlow<String> = mutableLog

    fun setWeight(index: Int, weight: String) = updateFunction(functionTerms.value.also { it[index] = it[index].copy(value = weight) })

    fun putArg() {
        updateFunction(functionTerms.value.toMutableList().apply { add(TermModel()) })
        putInequalityColumn()
    }

    fun dropArg() {
        if (functionTerms.value.size > MIN_TERMS) {
            updateFunction(functionTerms.value.toMutableList().apply { removeLast() })
            dropInequalityColumn()
        }
    }

    fun putInequalityRow() =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                add(InequalityRowModel(terms = MutableList(functionTerms.value.size) { TermModel() }))
            }
        )

    fun dropInequalityRow() {
        if (inequalities.value.size > MIN_INEQUALITIES) {
            updateInequalities(
                inequalities.value.toMutableList().apply { removeLast() }
            )
        }
    }

    fun switchMatrixSign(row: Int, col: Int) {
        val newTerm = inequalities.value[row].terms[col].run { copy(sign = sign.nextAddSign()) }
        setInequalityTerm(row, col, newTerm)
    }

    fun switchFunctionSign(index: Int) =
        updateFunction(
            functionTerms.value.toMutableList().apply {
                get(index).let { old ->
                    set(index, old.copy(sign = old.sign.nextAddSign()))
                }
            }
        )

    fun switchBoundsSign(row: Int) =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                set(row, get(row).copy(boundSign = get(row).boundSign.nextAddSign()))
            }
        )

    fun switchInequalitySign(row: Int) =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                set(row, get(row).copy(sign = get(row).sign.nextInequalitySign()))
            }
        )

    fun setBoundsWeight(row: Int, col: Int, weight: String) {
        val newTerm = inequalities.value[row].terms[col].copy(value = weight)
        setInequalityTerm(row, col, newTerm)
    }

    fun setBound(row: Int, value: String) =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                set(row, get(row).copy(bound = value))
            }
        )

    fun switchModeClicked() {
        mutableSolveMode.value = if (solveMode.value == MAX) MIN else MAX
        edited.value = true
    }

    fun solve() {
        if (edited.value)
            viewModelScope.launch(Dispatchers.IO) {
                edited.value = false
                val matrix: MutableList<List<Double>> = ArrayList()
                val prices: MutableList<Double> = ArrayList()
                val signs: MutableList<Sign> = ArrayList()
                val bounds: MutableList<Double> = ArrayList()

                try {
                    functionTerms.value.forEach { term ->
                        prices += term.run { value.toDouble() * term.sign.toNumericSign() }
                    }

                    inequalities.value.forEach { row ->
                        matrix += row.terms.map { term -> term.run { value.toDouble() * term.sign.toNumericSign() } }
                        bounds += row.run { bound.toDouble() * boundSign.toNumericSign() }
                        signs += row.sign.toInequalitySign()
                    }

                    val simplex = Simplex(matrix, prices, signs, bounds)
                    val logBuilder = ByteArrayOutputStream()
                    simplex.output = logBuilder
                    if (solveMode.value == MAX)
                        simplex.findMax()
                    else
                        simplex.findMin()
                    simplex.solve()
                    updateLog(logBuilder.toByteArray().toString(StandardCharsets.UTF_8))
                } catch (th: Throwable) {
                    logError(th)
                }
            }
        else
            updateLog(log.value)
    }

    private fun logError(th: Throwable) {
        updateLog(th.toString())
    }

    private fun updateLog(log: String) {
        mutableLog.value = log
    }

    private fun setInequalityTerm(row: Int, col: Int, newTerm: TermModel) =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                set(row, get(row).copy(terms = get(row).terms.toMutableList().also { terms -> terms[col] = newTerm }))
            }
        )

    private fun putInequalityColumn() =
        updateInequalities(
            inequalities.value.map {
                it.copy(terms = it.terms.toMutableList().apply { add(TermModel()) })
            }
        )

    private fun dropInequalityColumn() =
        updateInequalities(
            inequalities.value.map {
                it.copy(terms = it.terms.toMutableList().apply { removeLast() })
            }
        )

    private fun updateFunction(terms: List<TermModel>) {
        val newList = terms.toMutableList()
        mutableFunctionTerms.value = newList
        edited.value = true
    }

    private fun updateInequalities(inequalities: List<InequalityRowModel>) {
        mutableInequalities.value = inequalities.toMutableList()
        edited.value = true
    }
}

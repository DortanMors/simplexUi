package `in`.fom.simplexui

import `in`.fom.simplexui.model.InequalityRowModel
import `in`.fom.simplexui.model.TermModel
import `in`.fom.simplexui.utils.Defaults.MIN_INEQUALITIES
import `in`.fom.simplexui.utils.Defaults.MIN_TERMS
import `in`.fom.simplexui.utils.Defaults.defaultFunctionTerms
import `in`.fom.simplexui.utils.Defaults.defaultInequalities
import `in`.fom.simplexui.utils.nextAddSign
import `in`.fom.simplexui.utils.nextInequalitySign
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {

    private val mutableFunctionTerms: MutableStateFlow<MutableList<TermModel>> = MutableStateFlow(defaultFunctionTerms())
    val functionTerms: StateFlow<MutableList<TermModel>> = mutableFunctionTerms
    private val mutableInequalities: MutableStateFlow<MutableList<InequalityRowModel>> = MutableStateFlow(defaultInequalities())
    val inequalities: StateFlow<MutableList<InequalityRowModel>> = mutableInequalities

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
        val newTerm = inequalities.value[row].terms[col].apply { sign.nextAddSign() }
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

    private fun setInequalityTerm(row: Int, col: Int, newTerm: TermModel) =
        updateInequalities(
            inequalities.value.toMutableList().apply {
                set(row, get(row).copy(terms = get(row).terms.also { terms -> terms[col] = newTerm }))
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
    }

    private fun updateInequalities(inequalities: List<InequalityRowModel>) {
        mutableInequalities.value = inequalities.toMutableList()
    }
}
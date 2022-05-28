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

    fun setWeight(index: Int, weight: String) {
        mutableFunctionTerms.value = functionTerms.value.also { it[index] = it[index].copy(value = weight) }
    }

    fun putArg() {
        mutableFunctionTerms.value = functionTerms.value.apply { add(TermModel()) }
    }

    fun dropArg() {
        if (functionTerms.value.size > MIN_TERMS) {
            mutableFunctionTerms.value = functionTerms.value.apply { removeLast() }
        }
    }

    fun putInequalityRow() {
        mutableInequalities.value = inequalities.value.apply { add(InequalityRowModel()) }
    }

    fun dropInequalityRow() {
        if (inequalities.value.size > MIN_INEQUALITIES) {
            mutableInequalities.value = inequalities.value.apply { removeLast() }
        }
    }

    fun switchMatrixSign(row: Int, col: Int) {
        val newTerm = inequalities.value[row].terms[col].apply { sign.nextAddSign() }
        setInequalityTerm(row, col, newTerm)
    }

    fun switchFunctionSign(index: Int) {
        mutableFunctionTerms.value = functionTerms.value.apply {
            get(index).let { old ->
                set(index, old.copy(sign = old.sign.nextAddSign()))
            }
        }
    }

    fun switchBoundsSign(row: Int) {
        mutableInequalities.value = inequalities.value.apply {
            set(row, get(row).copy(boundSign = get(row).boundSign.nextAddSign()))
        }
    }

    fun switchInequalitySign(row: Int) {
        mutableInequalities.value = inequalities.value.apply {
            set(row, get(row).copy(sign = get(row).sign.nextInequalitySign()))
        }
    }

    fun setBoundsWeight(row: Int, col: Int, weight: String) {
        val newTerm = inequalities.value[row].terms[col].copy(value = weight)
        setInequalityTerm(row, col, newTerm)
    }

    fun setBound(row: Int, value: String) {
        mutableInequalities.value = inequalities.value.apply {
            set(row, get(row).copy(bound = value))
        }
    }

    private fun setInequalityTerm(row: Int, col: Int, newTerm: TermModel) {
        mutableInequalities.value = inequalities.value.apply {
            set(row, get(row).copy(terms = get(row).terms.also { terms -> terms[col] = newTerm }))
        }
    }
}
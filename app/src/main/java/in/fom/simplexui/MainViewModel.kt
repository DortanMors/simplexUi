package `in`.fom.simplexui

import androidx.lifecycle.ViewModel
import `in`.fom.simplexui.utils.Defaults.COEFFICIENT
import `in`.fom.simplexui.utils.Defaults.WEIGHT
import `in`.fom.simplexui.utils.Defaults.defaultAddSign
import `in`.fom.simplexui.utils.Defaults.defaultBoundsMatrix
import `in`.fom.simplexui.utils.Defaults.defaultBoundsSigns
import `in`.fom.simplexui.utils.Defaults.defaultBoundsVector
import `in`.fom.simplexui.utils.Defaults.defaultInequalitySigns
import `in`.fom.simplexui.utils.Defaults.defaultFunctionSigns
import `in`.fom.simplexui.utils.Defaults.defaultFunctionWeights
import `in`.fom.simplexui.utils.Defaults.defaultInequalitySign
import `in`.fom.simplexui.utils.Defaults.defaultMatrixSigns
import `in`.fom.simplexui.utils.nextAddSign
import `in`.fom.simplexui.utils.nextInequalitySign
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {

    private val mutableFunctionSigns: MutableStateFlow<MutableList<String>> = MutableStateFlow(defaultFunctionSigns)
    val functionSigns: StateFlow<MutableList<String>> = MutableStateFlow(defaultFunctionSigns)
    private val mutableMatrixSigns: MutableStateFlow<MutableList<MutableList<String>>> = MutableStateFlow(defaultMatrixSigns)
    val matrixSigns: StateFlow<MutableList<MutableList<String>>> = MutableStateFlow(defaultMatrixSigns)
    private val mutableBoundsSigns: MutableStateFlow<MutableList<String>> = MutableStateFlow(defaultBoundsSigns)
    val boundsSigns: StateFlow<MutableList<String>> = MutableStateFlow(defaultBoundsSigns)
    private val mutableInequalitySigns: MutableStateFlow<MutableList<String>> = MutableStateFlow(defaultInequalitySigns)
    val inequalitySigns: StateFlow<MutableList<String>> = mutableInequalitySigns
    private val mutableBoundsVector: MutableStateFlow<MutableList<String>> = MutableStateFlow(defaultBoundsVector)
    val boundsVector: StateFlow<MutableList<String>> = mutableBoundsVector
    private val mutableBoundsMatrix: MutableStateFlow<MutableList<MutableList<String>>> = MutableStateFlow(defaultBoundsMatrix)
    val boundsMatrix: StateFlow<MutableList<MutableList<String>>> = mutableBoundsMatrix
    private val mutableVectorWeights: MutableStateFlow<MutableList<String>> = MutableStateFlow(defaultFunctionWeights)
    val vectorWeights: StateFlow<MutableList<String>> = mutableVectorWeights

    fun setWeight(index: Int, value: String) {
        mutableVectorWeights.value = (vectorWeights.value.toMutableList().also { it[index] = value })
    }

    fun putArg(value: String = COEFFICIENT.toString()) {
        putFunctionCoefficient(value)
        putFunctionSign()
        putInequalityColumn()
    }

    private fun putFunctionCoefficient(value: String) {
        mutableVectorWeights.value = (vectorWeights.value.toMutableList().apply { add(value) })
    }

    private fun putFunctionSign() {
        mutableFunctionSigns.value = functionSigns.value.apply { add(defaultAddSign) }
    }

    private fun putInequalityColumn() {
        mutableBoundsMatrix.value = boundsMatrix.value.map { boundsRow ->
            boundsRow.apply { add("$WEIGHT") }
        }.toMutableList()
    }

    fun dropArg() {
        dropFunctionValue()
        dropFunctionSign()
        dropInequalityColumn()
    }

    private fun dropFunctionValue() {
        mutableVectorWeights.value = (vectorWeights.value.toMutableList().apply { removeLast() })
    }

    private fun dropFunctionSign() {
        mutableFunctionSigns.value = functionSigns.value.apply { removeLast() }
    }

    private fun dropInequalityColumn() {
        mutableBoundsMatrix.value = boundsMatrix.value.map { boundsRow ->
            boundsRow.apply { removeLast() }
        }.toMutableList()
    }

    fun putInequalityRow() {
        putMatrixValues()
        putMatrixSigns()
        putSign()
        putBound()
    }

    private fun putMatrixValues() {
        val row = MutableList(vectorWeights.value.size) { WEIGHT.toString() }
        mutableBoundsMatrix.value = boundsMatrix.value.apply { add(row) }
    }
    
    private fun putMatrixSigns() {
        val row = MutableList(vectorWeights.value.size) { defaultAddSign }
        mutableMatrixSigns.value = matrixSigns.value.apply { add(row) }
    }

    private fun putSign() {
        mutableInequalitySigns.value = inequalitySigns.value.apply { add(defaultInequalitySign) }
    }

    private fun putBound() {
        putBoundValue()
        putBoundSign()
    }

    private fun putBoundValue() {
        mutableBoundsVector.value = boundsVector.value.apply { add(WEIGHT.toString()) }
    }

    private fun putBoundSign() {
        mutableBoundsSigns.value = boundsSigns.value.apply { add(defaultInequalitySign) }
    }

    fun dropInequalityRow() {
        dropMatrixRow()
        dropMatrixSigns()
        dropSign()
        dropBound()
    }

    private fun dropMatrixRow() {
        mutableBoundsMatrix.value = boundsMatrix.value.apply { removeLast() }
    }
    
    private fun dropMatrixSigns() {
        mutableMatrixSigns.value = matrixSigns.value.apply { removeLast() }
    }

    private fun dropSign() {
        mutableInequalitySigns.value = inequalitySigns.value.apply { removeLast() }
    }

    private fun dropBound() {
        dropBoundValue()
        dropBoundSign()
    }

    private fun dropBoundValue() {
        mutableBoundsVector.value = boundsVector.value.apply { removeLast() }
    }

    private fun dropBoundSign() {
        mutableBoundsSigns.value = boundsSigns.value.apply { removeLast() }
    }

    fun switchMatrixSign(row: Int, col: Int) {
        mutableMatrixSigns.value = matrixSigns.value.also { it[row][col] = it[row][col].nextAddSign() }
    }

    fun switchFunctionSign(index: Int) {
        mutableFunctionSigns.value = functionSigns.value.apply { set(index, get(index).nextAddSign()) }
    }

    fun switchBoundsSign(row: Int) {
        mutableBoundsSigns.value = boundsSigns.value.apply { set(row, get(row).nextAddSign()) }
    }

    fun switchInequalitySign(row: Int) {
        mutableInequalitySigns.value = inequalitySigns.value.apply { set(row, get(row).nextInequalitySign()) }
    }

    fun setBoundsWeight(row: Int, col: Int, value: String) {
        mutableBoundsMatrix.value = boundsMatrix.value.also { it[row][col] = value }
    }

    fun setBound(row: Int, value: String) {
        mutableBoundsVector.value = boundsVector.value.apply { set(row, value) }
    }
}
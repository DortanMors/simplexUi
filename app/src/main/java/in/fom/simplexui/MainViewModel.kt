package `in`.fom.simplexui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    companion object {
        const val DEFAULT_COEFFICIENT = 0.0
    }

    val mutableVector: MutableStateFlow<MutableList<String>> = MutableStateFlow(mutableListOf("1.0"))

    fun setFunctionCoefficient(index: Int, value: String) {
        mutableVector.value = (mutableVector.value.toMutableList().also { it[index] = value })
        Log.d("HARDCODE", mutableVector.value.toString())
    }

    fun putArg(value: String = "$DEFAULT_COEFFICIENT") {
       mutableVector.value = (mutableVector.value.toMutableList().apply { add(value) })
    }

    fun dropArg() {
        mutableVector.value = (mutableVector.value.toMutableList().apply { removeLast() })
    }

}
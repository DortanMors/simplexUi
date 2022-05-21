package `in`.fom.simplexui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val mutableSize: MutableStateFlow<Int> = MutableStateFlow(1)

    val mutableVector: MutableStateFlow<MutableList<Double>> = MutableStateFlow(mutableListOf(1.0))

    fun setFunctionCoefficient(index: Int, value: Double) {
        viewModelScope.launch { mutableVector.emit(mutableVector.value.toMutableList().also { it[index] = value }) }
        Log.d("HARDCODE", mutableVector.value.toString())
    }

}
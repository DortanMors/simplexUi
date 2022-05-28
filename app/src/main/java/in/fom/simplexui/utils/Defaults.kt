package `in`.fom.simplexui.utils

import com.ssau.lib.simplex.Sign

object Defaults {
    const val COEFFICIENT = 0.0
    const val WEIGHT = 1.0
    val SIGN = Sign.Less
    val PLUS = "+"
    val MINUS = "-"
    val LESS = "<"
    val EQUALS = "="
    val GREATER = ">"

    val defaultAddSign = PLUS
    val defaultInequalitySign = SIGN.toSymbol()

    val algebraSigns = listOf(MINUS, PLUS)
    val inequalitySigns = listOf(LESS, EQUALS, GREATER)
    val defaultFunctionSigns = mutableListOf(PLUS)
    val defaultInequalitySigns = mutableListOf(SIGN.toSymbol())
    val defaultBoundsVector= mutableListOf(WEIGHT.toString())
    val defaultBoundsMatrix = mutableListOf(mutableListOf(WEIGHT.toString()))
    val defaultFunctionWeights = mutableListOf(WEIGHT.toString())
    val defaultMatrixSigns = mutableListOf(mutableListOf(PLUS))
    val defaultBoundsSigns = mutableListOf(PLUS)
}
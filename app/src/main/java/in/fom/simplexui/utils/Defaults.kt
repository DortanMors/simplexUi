package `in`.fom.simplexui.utils

import `in`.fom.simplexui.model.InequalityRowModel
import `in`.fom.simplexui.model.TermModel
import com.ssau.lib.simplex.Sign

object Defaults {
    const val WEIGHT = 1.0
    const val MIN_TERMS = 1
    const val MIN_INEQUALITIES = 1
    private val SIGN = Sign.Less
    const val PLUS = "+"
    const val MINUS = "-"
    const val LESS = "<"
    const val EQUALS = "="
    const val GREATER = ">"

    const val defaultIndex = 0
    const val defaultAddSign = PLUS
    fun defaultInequalitySign() = SIGN.toSymbol()

    fun defaultFunctionTerms() = mutableListOf(TermModel())
    fun defaultInequalities() = mutableListOf(InequalityRowModel())

    val algebraSigns = listOf(MINUS, PLUS)
    val inequalitySigns = listOf(LESS, EQUALS, GREATER)
}
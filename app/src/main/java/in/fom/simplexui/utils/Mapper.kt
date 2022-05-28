package `in`.fom.simplexui.utils

import `in`.fom.simplexui.utils.Defaults.EQUALS
import `in`.fom.simplexui.utils.Defaults.GREATER
import `in`.fom.simplexui.utils.Defaults.LESS
import `in`.fom.simplexui.utils.Defaults.MINUS
import `in`.fom.simplexui.utils.Defaults.PLUS
import `in`.fom.simplexui.utils.Defaults.algebraSigns
import `in`.fom.simplexui.utils.Defaults.inequalitySigns
import com.ssau.lib.simplex.Sign

fun String.toNumericSign() = if (this == MINUS) -1 else 1

fun String.toInequalitySign() =
    when (this) {
        GREATER -> Sign.Greater
        EQUALS  -> Sign.Equal
        else    -> Sign.Less

    }

fun Sign.toSymbol() =
    when (this) {
        Sign.Greater -> GREATER
        Sign.Equal   -> EQUALS
        Sign.Less    -> LESS
    }

fun Int.toSign() = if (this > 0) PLUS else MINUS

fun String.nextAddSign() = algebraSigns.rollElement(this)

fun String.nextInequalitySign() = inequalitySigns.rollElement(this)

private fun List<String>.rollElement(value: String) = get((indexOf(value) + 1) % size)

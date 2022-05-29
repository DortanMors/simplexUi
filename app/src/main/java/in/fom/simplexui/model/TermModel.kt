package `in`.fom.simplexui.model

import `in`.fom.simplexui.utils.Defaults.WEIGHT
import `in`.fom.simplexui.utils.Defaults.defaultAddSign
import `in`.fom.simplexui.utils.Defaults.defaultIndex

data class TermModel(
    val sign:  String = defaultAddSign,
    val value: String = WEIGHT.toString(),
    val index: Int = defaultIndex
)

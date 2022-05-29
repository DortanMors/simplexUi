package `in`.fom.simplexui.model

import `in`.fom.simplexui.utils.Defaults.WEIGHT
import `in`.fom.simplexui.utils.Defaults.defaultAddSign
import `in`.fom.simplexui.utils.Defaults.defaultFunctionTerms
import `in`.fom.simplexui.utils.Defaults.defaultInequalitySign

data class InequalityRowModel(
    val terms:     MutableList<TermModel> = defaultFunctionTerms(),
    val sign:      String                 = defaultInequalitySign(),
    val boundSign: String                 = defaultAddSign,
    val bound:     String                 = WEIGHT.toString()
)

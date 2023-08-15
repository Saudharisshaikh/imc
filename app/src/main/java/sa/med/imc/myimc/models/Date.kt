package sa.med.imc.myimc.models

data class Date (
    val day: String,
    val date: Int,
    val month: Int = 0,
    var isSelected: Boolean = false,
    var isDisabled: Boolean = false
)
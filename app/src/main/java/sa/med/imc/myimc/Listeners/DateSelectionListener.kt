package sa.med.imc.myimc.Listeners

import sa.med.imc.myimc.models.Date

interface DateSelectionListener {
    fun onSelected(date: Date)
}
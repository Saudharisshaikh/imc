package sa.med.imc.myimc.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Listeners.DateSelectionListener
import sa.med.imc.myimc.R
import sa.med.imc.myimc.databinding.ItemDateBinding
import sa.med.imc.myimc.models.Date

class DatesAdapter(val context: Context, val dates: List<Date>, val currentDate: Int, val listener: DateSelectionListener) : RecyclerView.Adapter<DatesAdapter.DateViewHolder>() {

    private var lastSelectedItemIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder =
        DateViewHolder(ItemDateBinding.inflate(LayoutInflater.from(context),parent, false))

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) =
        holder.bind(dates.get(position))

    override fun getItemCount(): Int = dates.size

    override fun getItemViewType(position: Int): Int = position

    inner class DateViewHolder(val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: Date) {
            binding.day.text = date.day
            binding.date.text = date.date.toString()

            if (date.isDisabled) {
                binding.child.setOnClickListener(null)
                binding.day.alpha = 0.5f
                binding.date.alpha = 0.5f
            } else {
                binding.child.setOnClickListener {

                    if (lastSelectedItemIndex != RecyclerView.NO_POSITION) {
                        dates.get(lastSelectedItemIndex).isSelected = false
                        notifyItemChanged(lastSelectedItemIndex)

                        lastSelectedItemIndex = adapterPosition
                        date.isSelected = true
                        notifyItemChanged(adapterPosition)

                        listener.onSelected(dates.get(adapterPosition))
                    } else {
                        lastSelectedItemIndex = adapterPosition
                        date.isSelected = true

                        notifyItemChanged(adapterPosition)

                        listener.onSelected(dates.get(adapterPosition))
                    }
                }
            }

            doSelectCurrentDate()

            if (date.isSelected) {
                binding.date.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.bgDate.visibility = View.VISIBLE
            } else {
                binding.date.setTextColor(ContextCompat.getColor(context, R.color.black))
                binding.bgDate.visibility = View.INVISIBLE
            }
        }
    }

    private fun doSelectCurrentDate() {
        if (lastSelectedItemIndex == -1) {
            lastSelectedItemIndex = currentDate
            dates.get(lastSelectedItemIndex).isSelected = true
        }
    }
}
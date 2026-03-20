package com.jaques.projetos.organizze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.databinding.AdapterMovementBinding
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.model.MovementType
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MovementAdapter(private val list: MutableList<Movement>) :
    RecyclerView.Adapter<MovementAdapter.ViewHolder>() {

    private val currencyFormat = DecimalFormat(
        "#,##0.00",
        DecimalFormatSymbols(Locale("pt", "BR"))
    )

    private var expenseColor: Int = 0
    private var revenueColor: Int = 0

    inner class ViewHolder(private val binding: AdapterMovementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movement: Movement) {
            if (expenseColor == 0) {
                expenseColor = ContextCompat.getColor(itemView.context, R.color.expense)
                revenueColor = ContextCompat.getColor(itemView.context, R.color.revenue)
            }

            binding.textAdapterTitle.text = movement.description
            binding.textAdapterCategoy.text = movement.category

            when (movement.type) {
                MovementType.EXPENSE -> {
                    binding.textAdapterValue.text = "- R$ ${currencyFormat.format(movement.value)}"
                    binding.textAdapterValue.setTextColor(expenseColor)
                }
                MovementType.REVENUE -> {
                    binding.textAdapterValue.text = "R$ ${currencyFormat.format(movement.value)}"
                    binding.textAdapterValue.setTextColor(revenueColor)
                }
            }
        }
    }

    fun submitList(newList: List<Movement>) {
        val diffResult = DiffUtil.calculateDiff(MovementDiffCallback(list, newList))
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMovementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private class MovementDiffCallback(
        private val oldList: List<Movement>,
        private val newList: List<Movement>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
    }
}

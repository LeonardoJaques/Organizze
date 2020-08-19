package com.jaques.projetos.organizze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.model.Movement

/** author Leonardo Jaques on 18/08/20 */
class MovementAdapter(
    listMovement: ArrayList<Movement>

) :
    RecyclerView.Adapter<MovementAdapter.MyViewHolder>() {
    private val list = listMovement

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textAdapterTitle)
        val value: TextView = itemView.findViewById(R.id.textAdapterValue)
        val category: TextView = itemView.findViewById(R.id.textAdapterCategoy)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemList: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_movement, parent, false)
        return MyViewHolder(itemList)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movement: Movement = list[position]
        holder.title.text = movement.description
        holder.value.text = movement.value.toString()
        holder.category.text = movement.category

        if (movement.type.equals("e")) {
            holder.value.textColors.getColorForState(intArrayOf(), R.color.colorAccent)
            holder.value.text = "- ${movement.value}"
        }
    }

}



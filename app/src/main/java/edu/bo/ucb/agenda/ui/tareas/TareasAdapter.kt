package edu.bo.ucb.agenda.ui.tareas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.databinding.ItemTareaBinding

class TareasAdapter(private val listener: onItemClickListener) : ListAdapter<Tarea, TareasAdapter.TareasViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val  binding = edu.bo.ucb.agenda.databinding.ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
        val itemActual = getItem(position)
        holder.bind(itemActual)
    }

    inner class TareasViewHolder(private val binding: ItemTareaBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val tarea = getItem(position)
                        listener.onItemClick(tarea)
                    }
                }
                checkboxCompletado.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val tarea = getItem(position)
                        listener.onCheckBoxClick(tarea, checkboxCompletado.isChecked)
                    }
                }
            }
        }
        fun bind(tarea: Tarea){
            binding.apply {
                checkboxCompletado.isChecked = tarea.completada
                textViewNombre.text = tarea.nombre
                textViewNombre.paint.isStrikeThruText = tarea.completada
                labelPrioridad.isVisible = tarea.importante
            }
        }
    }
    interface onItemClickListener{
        fun onItemClick(tarea: Tarea)
        fun onCheckBoxClick(tarea: Tarea,isChecked:Boolean)
    }
    class DiffCallback : DiffUtil.ItemCallback<Tarea>(){
        override fun areItemsTheSame(oldItem: Tarea, newItem: Tarea) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Tarea, newItem: Tarea) =
            oldItem == newItem
    }
}
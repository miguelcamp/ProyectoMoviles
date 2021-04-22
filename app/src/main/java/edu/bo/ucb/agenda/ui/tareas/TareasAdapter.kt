package edu.bo.ucb.agenda.ui.tareas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.databinding.ItemTareaBinding

class TareasAdapter : ListAdapter<Tarea, TareasAdapter.TareasViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val  binding = edu.bo.ucb.agenda.databinding.ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
        val itemActual = getItem(position)
        holder.bind(itemActual)
    }

    class TareasViewHolder(private val binding: ItemTareaBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(tarea: Tarea){
            binding.apply {
                checkboxCompletado.isChecked = tarea.completada
                textViewNombre.text = tarea.nombre
                textViewNombre.paint.isStrikeThruText = tarea.completada
                labelPrioridad.isVisible = tarea.importante
            }
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<Tarea>(){
        override fun areItemsTheSame(oldItem: Tarea, newItem: Tarea) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Tarea, newItem: Tarea) =
            oldItem == newItem
    }
}
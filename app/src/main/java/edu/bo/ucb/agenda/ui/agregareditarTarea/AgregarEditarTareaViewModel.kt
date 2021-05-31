package edu.bo.ucb.agenda.ui.agregareditarTarea

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.data.TareaDao

class AgregarEditarTareaViewModel @ViewModelInject constructor(
    private val tareaDao: TareaDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val tarea = state.get<Tarea>("tarea")

    var nombreTarea = state.get<String>("nombreTarea") ?: tarea?.nombre ?: ""
        set(value){
            field = value
            state.set("nombreTarea",value)
        }

    var tareaImportante = state.get<Boolean>("tareaImportante") ?: tarea?.importante ?: false
        set(value){
            field = value
            state.set("tareaImportante",value)
        }
}
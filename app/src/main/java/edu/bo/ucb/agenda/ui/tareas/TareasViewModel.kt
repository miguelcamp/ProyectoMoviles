package edu.bo.ucb.agenda.ui.tareas

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.bo.ucb.agenda.data.ControladorPreferencias
import edu.bo.ucb.agenda.data.OrdenFiltro
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.data.TareaDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TareasViewModel @ViewModelInject constructor(
    private val tareaDao : TareaDao,
    private val controladorPreferencias: ControladorPreferencias
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val flowPreferencias = controladorPreferencias.flowPreferencias

    private val tasksFlow = combine(
        searchQuery,
        flowPreferencias

    ) { query, filtrarPreferencias ->
        Pair(query, filtrarPreferencias)
    }.flatMapLatest { (query, filtrarPreferencias) ->
        tareaDao.getTareas(query, filtrarPreferencias.ordenFiltro, filtrarPreferencias.ocultarCompletadas)
    }

    val tareas = tasksFlow.asLiveData()


    fun alSeleccionarOrdenFiltro(ordenFiltro: OrdenFiltro) = viewModelScope.launch {
        controladorPreferencias.actualizarOrdenFiltro(ordenFiltro)
    }

    fun alSeleccionarOcultarCompletadas(ocultarCompletadas: Boolean) = viewModelScope.launch {
        controladorPreferencias.actualizarOcultarCompletadas(ocultarCompletadas)
    }

    fun alSeleccionarTarea(tarea: Tarea){

    }

    fun alCambiarCheckTarea(tarea: Tarea, isChecked: Boolean) = viewModelScope.launch {
        tareaDao.update(tarea.copy(completada = isChecked))
    }



}

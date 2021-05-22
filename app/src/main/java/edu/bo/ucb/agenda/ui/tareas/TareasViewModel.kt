package edu.bo.ucb.agenda.ui.tareas

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import edu.bo.ucb.agenda.data.TareaDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest

class TareasViewModel @ViewModelInject constructor(
    private val tareaDao : TareaDao
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val ordenFiltro = MutableStateFlow(OrdenFiltro.POR_FECHA)
    val ocultarCompletadas = MutableStateFlow(false)

    private val tasksFlow = combine(
        searchQuery,
        ordenFiltro,
        ocultarCompletadas
    ) { query, ordenFiltro, ocultarCompletadas ->
        Triple(query, ordenFiltro, ocultarCompletadas)
    }.flatMapLatest { (query, ordenFiltro, ocultarCompletadas) ->
        tareaDao.getTareas(query, ordenFiltro, ocultarCompletadas)
    }

    val tareas = tasksFlow.asLiveData()
}
enum class OrdenFiltro { POR_NOMBRE, POR_FECHA }
package edu.bo.ucb.agenda.ui.tareas

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import edu.bo.ucb.agenda.data.ControladorPreferencias
import edu.bo.ucb.agenda.data.OrdenFiltro
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.data.TareaDao
import edu.bo.ucb.agenda.ui.AÑADIR_TAREA_RESULTADO_CORRECTO
import edu.bo.ucb.agenda.ui.EDITAR_TAREA_RESULTADO_CORRECTO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TareasViewModel @ViewModelInject constructor(
    private val tareaDao : TareaDao,
    private val controladorPreferencias: ControladorPreferencias,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery","")

    val flowPreferencias = controladorPreferencias.flowPreferencias

    private val CanalEventoTareas = Channel<EventoTareas>()
    val eventoTareas = CanalEventoTareas.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
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

    fun alSeleccionarTarea(tarea: Tarea) = viewModelScope.launch {
        CanalEventoTareas.send(EventoTareas.NavegarAPantallaEditar(tarea))
    }

    fun alCambiarCheckTarea(tarea: Tarea, isChecked: Boolean) = viewModelScope.launch {
        tareaDao.update(tarea.copy(completada = isChecked))
    }

    fun alHacerSwipe(tarea: Tarea) = viewModelScope.launch {
        tareaDao.delete(tarea)
        CanalEventoTareas.send(EventoTareas.MostrarMensajeDeshacer(tarea))
    }

    fun alDeshacer(tarea: Tarea) = viewModelScope.launch {
        tareaDao.insert(tarea)
    }

    fun alPresionarAgregarTarea() = viewModelScope.launch {
        CanalEventoTareas.send(EventoTareas.NavegarAPantallaAgregar)
    }

    fun alAñadirEditarResultado(resultado:Int){
        when(resultado){
            AÑADIR_TAREA_RESULTADO_CORRECTO -> mostrarMensajeConfirmacionTareaGuardada("Tarea añadida")
            EDITAR_TAREA_RESULTADO_CORRECTO -> mostrarMensajeConfirmacionTareaGuardada("Tarea actualizada")
        }
    }

    private fun mostrarMensajeConfirmacionTareaGuardada(text: String) = viewModelScope.launch {
        CanalEventoTareas.send(EventoTareas.MostrarMensajeConfirmacionTareaGuardada(text))
    }

    fun alClickearBorrarTodasLasCompletadas() = viewModelScope.launch {
        CanalEventoTareas.send(EventoTareas.NavegarAPantallaDeBorrarTodasLasCompletadas)
    }
    sealed class EventoTareas {
        object NavegarAPantallaAgregar : EventoTareas()
        data class NavegarAPantallaEditar(val tarea: Tarea) : EventoTareas()
        data class MostrarMensajeDeshacer(val tarea: Tarea) : EventoTareas()
        data class MostrarMensajeConfirmacionTareaGuardada(val msg: String) : EventoTareas()
        object NavegarAPantallaDeBorrarTodasLasCompletadas : EventoTareas()
    }



}

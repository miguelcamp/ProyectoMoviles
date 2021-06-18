package edu.bo.ucb.agenda.ui.agregareditarTarea

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.data.TareaDao
import edu.bo.ucb.agenda.ui.AÑADIR_TAREA_RESULTADO_CORRECTO
import edu.bo.ucb.agenda.ui.EDITAR_TAREA_RESULTADO_CORRECTO
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


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

    var fechaLimite = state.get<String>("fechaLimite") ?: tarea?.fechaLimite ?:""
        set(value){
            field = value
            state.set("fechaLimite",value)
        }
    private val canalEventoAgregarEditarTarea = Channel<EventoAgregarEditarTarea>()
    val eventoAgregarEditarTarea = canalEventoAgregarEditarTarea.receiveAsFlow()

    fun onSaveClick() {
        if (nombreTarea.isBlank()) {
            mostrarMensajeDeEntradaInvalida("El nombre no puede estar vacio")
            return
        }

        if (tarea != null) {
            val tareaActualizada = tarea.copy(nombre = nombreTarea, importante = tareaImportante, fechaLimite = fechaLimite)
            actualizarTarea(tareaActualizada)
        } else {
            val nuevaTarea = Tarea(nombre = nombreTarea, importante = tareaImportante, fechaLimite = fechaLimite)
            crearTarea(nuevaTarea)
        }
    }

    private fun crearTarea(tarea: Tarea) = viewModelScope.launch {
        tareaDao.insert(tarea)
        canalEventoAgregarEditarTarea.send(EventoAgregarEditarTarea.NavegarAtrasConElResultado(AÑADIR_TAREA_RESULTADO_CORRECTO))
    }

    private fun actualizarTarea(tarea: Tarea) = viewModelScope.launch {
        tareaDao.update(tarea)
        canalEventoAgregarEditarTarea.send(EventoAgregarEditarTarea.NavegarAtrasConElResultado(EDITAR_TAREA_RESULTADO_CORRECTO))
    }

    private fun mostrarMensajeDeEntradaInvalida(text: String) = viewModelScope.launch {
        canalEventoAgregarEditarTarea.send(EventoAgregarEditarTarea.MostrarMensajeDeEntradaInvalida(text))
    }

    sealed class EventoAgregarEditarTarea {
        data class MostrarMensajeDeEntradaInvalida(val msg: String) : EventoAgregarEditarTarea()
        data class NavegarAtrasConElResultado(val result: Int) : EventoAgregarEditarTarea()
    }
}
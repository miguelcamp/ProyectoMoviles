package edu.bo.ucb.agenda.ui.tareas

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import edu.bo.ucb.agenda.data.TareaDao

class TareasViewModel @ViewModelInject constructor(
    private val tareaDao : TareaDao
) : ViewModel() {
    val tareas = tareaDao.getTareas().asLiveData()
}
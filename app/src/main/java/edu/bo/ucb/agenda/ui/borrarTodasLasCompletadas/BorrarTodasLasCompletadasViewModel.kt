package edu.bo.ucb.agenda.ui.borrarTodasLasCompletadas

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import edu.bo.ucb.agenda.data.TareaDao
import edu.bo.ucb.agenda.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BorrarTodasLasCompletadasViewModel @ViewModelInject constructor(
        private val tareaDao: TareaDao,
        @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun alConfirmarClick() = applicationScope.launch {
        tareaDao.deleteCompletedTasks()
    }
}
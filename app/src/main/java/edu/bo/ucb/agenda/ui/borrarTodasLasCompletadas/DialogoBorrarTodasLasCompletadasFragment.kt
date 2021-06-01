package edu.bo.ucb.agenda.ui.borrarTodasLasCompletadas
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogoBorrarTodasLasCompletadasFragment : DialogFragment() {

    private val viewModel: BorrarTodasLasCompletadasViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estas seguro de que deseas borrar todas las tareas completadas?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Si") { _, _ ->
                        viewModel.alConfirmarClick()
                    }
                    .create()
}
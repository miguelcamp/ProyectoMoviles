package edu.bo.ucb.agenda.ui.agregareditarTarea

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.databinding.FragmentAnadirEditarTareaBinding

@AndroidEntryPoint
class AgregarEditarTareaFragment : Fragment(R.layout.fragment_anadir_editar_tarea) {

    private val viewModel: AgregarEditarTareaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAnadirEditarTareaBinding.bind(view)

        binding.apply {
            editTextTarea.setText(viewModel.nombreTarea)
            checkboxImportante.isChecked = viewModel.tareaImportante
            checkboxImportante.jumpDrawablesToCurrentState()
            textViewFechaCreada.isVisible = viewModel.tarea != null
            textViewFechaCreada.text = "Creado: ${viewModel.tarea?.fechaCreacionFormateada}"
        }
    }
}

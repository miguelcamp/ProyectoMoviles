package edu.bo.ucb.agenda.ui.agregareditarTarea

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.databinding.FragmentAnadirEditarTareaBinding
import edu.bo.ucb.agenda.util.exhaustive
import kotlinx.coroutines.flow.collect
import androidx.navigation.fragment.findNavController

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

            textViewFechaLimite.isVisible = viewModel.tarea != null
            textViewFechaLimite.text =  viewModel.fechaLimite

            editTextTarea.addTextChangedListener {
                viewModel.nombreTarea = it.toString()
            }

            checkboxImportante.setOnCheckedChangeListener { _, isChecked ->
                viewModel.tareaImportante = isChecked
            }

            fabGuardarTarea.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventoAgregarEditarTarea.collect { event ->
                when (event) {
                    is AgregarEditarTareaViewModel.EventoAgregarEditarTarea.MostrarMensajeDeEntradaInvalida -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AgregarEditarTareaViewModel.EventoAgregarEditarTarea.NavegarAtrasConElResultado -> {
                        binding.editTextTarea.clearFocus()
                        setFragmentResult(
                                "añadir_editar_request",
                                bundleOf("añadir_editar_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}


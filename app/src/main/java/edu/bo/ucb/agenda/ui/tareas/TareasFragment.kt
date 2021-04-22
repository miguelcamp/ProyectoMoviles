package edu.bo.ucb.agenda.ui.tareas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.databinding.FragmentTareasBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TareasFragment : Fragment(R.layout.fragment_tareas) {
    private val viewModel: TareasViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTareasBinding.bind(view)

        val tareasAdapter = TareasAdapter()

        binding.apply {
            recyclerViewTareas.apply {
                adapter = tareasAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        viewModel.tareas.observe(viewLifecycleOwner) {
            tareasAdapter.submitList(it)
        }
    }
}
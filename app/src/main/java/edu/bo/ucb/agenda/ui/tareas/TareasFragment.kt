package edu.bo.ucb.agenda.ui.tareas

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.databinding.FragmentTareasBinding
import dagger.hilt.android.AndroidEntryPoint
import edu.bo.ucb.agenda.util.onQueryTextChange

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
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tareas, menu)

        val buscarItem = menu.findItem(R.id.accion_buscar)
        val searchView = buscarItem.actionView as SearchView

        searchView.onQueryTextChange {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.accion_ordenar_por_nombre -> {
                viewModel.ordenFiltro.value = OrdenFiltro.POR_NOMBRE
                true
            }
            R.id.accion_ordenar_por_fecha_de_creacion -> {
                viewModel.ordenFiltro.value = OrdenFiltro.POR_FECHA
                true
            }
            R.id.accion_ocultar_las_completadas -> {
                item.isChecked = !item.isChecked
                viewModel.ocultarCompletadas.value = item.isChecked
                true
            }
            R.id.accion_borrar_todas_las_tareas_completadas -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
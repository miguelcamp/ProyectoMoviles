package edu.bo.ucb.agenda.ui.tareas

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.databinding.FragmentTareasBinding
import dagger.hilt.android.AndroidEntryPoint
import edu.bo.ucb.agenda.data.OrdenFiltro
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.util.onQueryTextChange
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TareasFragment : Fragment(R.layout.fragment_tareas), TareasAdapter.onItemClickListener {
    private val viewModel: TareasViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTareasBinding.bind(view)

        val tareasAdapter = TareasAdapter(this)

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

    override fun onItemClick(tarea: Tarea) {
        viewModel.alSeleccionarTarea(tarea)
    }

    override fun onCheckBoxClick(tarea: Tarea, isChecked: Boolean) {
        viewModel.alCambiarCheckTarea(tarea,isChecked)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tareas, menu)

        val buscarItem = menu.findItem(R.id.accion_buscar)
        val searchView = buscarItem.actionView as SearchView

        searchView.onQueryTextChange {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch{
            menu.findItem(R.id.accion_ocultar_las_completadas).isChecked =
                viewModel.flowPreferencias.first().ocultarCompletadas
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.accion_ordenar_por_nombre -> {
                viewModel.alSeleccionarOrdenFiltro(OrdenFiltro.POR_NOMBRE)
                true
            }
            R.id.accion_ordenar_por_fecha_de_creacion -> {
                viewModel.alSeleccionarOrdenFiltro(OrdenFiltro.POR_FECHA)
                true
            }
            R.id.accion_ocultar_las_completadas -> {
                item.isChecked = !item.isChecked
                viewModel.alSeleccionarOcultarCompletadas(item.isChecked)
                true
            }
            R.id.accion_borrar_todas_las_tareas_completadas -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
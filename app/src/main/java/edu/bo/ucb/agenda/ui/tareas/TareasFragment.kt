package edu.bo.ucb.agenda.ui.tareas

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.data.OrdenFiltro
import edu.bo.ucb.agenda.data.Tarea
import edu.bo.ucb.agenda.databinding.FragmentTareasBinding
import edu.bo.ucb.agenda.util.exhaustive
import edu.bo.ucb.agenda.util.onQueryTextChange
import kotlinx.android.synthetic.main.fragment_tareas.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TareasFragment : Fragment(R.layout.fragment_tareas), TareasAdapter.onItemClickListener {
    private val viewModel: TareasViewModel by viewModels()
    var date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    private lateinit var searchView:SearchView

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
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val tarea = tareasAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.alHacerSwipe(tarea)
                }
            }).attachToRecyclerView(recyclerViewTareas)

            fab_añadir_tarea.setOnClickListener{
                viewModel.alPresionarAgregarTarea()
            }
        }

        setFragmentResultListener("añadir_editar_request") { _, bundle ->
            val resultado = bundle.getInt("añadir_editar_result")
            viewModel.alAñadirEditarResultado(resultado)
        }

        viewModel.tareas.observe(viewLifecycleOwner) {
            tareasAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventoTareas.collect{ event ->
                when (event){
                    is TareasViewModel.EventoTareas.MostrarMensajeDeshacer -> {
                        Snackbar.make(requireView(),"Tarea eliminada", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer"){
                                viewModel.alDeshacer(event.tarea)
                            }.show()
                    }
                    is TareasViewModel.EventoTareas.NavegarAPantallaAgregar -> {
                     val action = TareasFragmentDirections.actionTareasFragmentToAgregarEditarTareaFragment(null,"Nueva Tarea")
                        findNavController().navigate(action)
                    }
                    is TareasViewModel.EventoTareas.NavegarAPantallaEditar -> {
                       val action = TareasFragmentDirections.actionTareasFragmentToAgregarEditarTareaFragment(event.tarea,"Editar Tarea")
                        findNavController().navigate(action)
                    }
                    is TareasViewModel.EventoTareas.MostrarMensajeConfirmacionTareaGuardada -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    TareasViewModel.EventoTareas.NavegarAPantallaDeBorrarTodasLasCompletadas -> {
                       val action = TareasFragmentDirections.actionGlobalDialogoBorrarTodasLasCompletadasFragment()
                        findNavController().navigate(action)
                    }

                    TareasViewModel.EventoTareas.NavegarACalendario -> {
                        val action = TareasFragmentDirections.actionTareasFragmentToCalendarioFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
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
        searchView = buscarItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if(pendingQuery != null && pendingQuery.isNotEmpty()){
            buscarItem.expandActionView()
            searchView.setQuery(pendingQuery,false)
        }
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
                viewModel.alClickearBorrarTodasLasCompletadas()
                true
            }
            R.id.mostrar_calendario -> {
                viewModel.alClickearCalendario()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}
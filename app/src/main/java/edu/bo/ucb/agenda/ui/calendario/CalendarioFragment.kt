package edu.bo.ucb.agenda.ui.calendario

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.bo.ucb.agenda.R
import edu.bo.ucb.agenda.ui.tareas.TareasViewModel
import kotlinx.android.synthetic.main.fragment_calendario.*
import kotlinx.coroutines.channels.Channel
import java.text.SimpleDateFormat
import java.util.*

class CalendarioFragment : Fragment(R.layout.fragment_calendario) {
    private val viewModel: TareasViewModel by viewModels()
    var date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewFechaSeleccionada.text = date

    }
}
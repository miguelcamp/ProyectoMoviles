package edu.bo.ucb.agenda.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ControladorPreferencias"
enum class OrdenFiltro { POR_NOMBRE, POR_FECHA }
data class FiltrarPreferencias(val ordenFiltro: OrdenFiltro, val ocultarCompletadas: Boolean)
@Singleton
class ControladorPreferencias @Inject constructor(@ApplicationContext context: Context)  {
    private val dataStore = context.createDataStore("preferencias_usuario")
    val flowPreferencias = dataStore.data
        .catch {exception ->
            if(exception is IOException){
                Log.e(TAG,"Error al leer las preferencias")
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map {preferences ->
            val ordenFiltro = OrdenFiltro.valueOf(
                preferences[PreferencesKeys.ORDEN_FILTRO] ?: OrdenFiltro.POR_FECHA.name
            )
            val ocultarCompletadas = preferences[PreferencesKeys.OCULTAR_COMPLETADAS] ?: false
            FiltrarPreferencias(ordenFiltro, ocultarCompletadas)
        }

    suspend fun  actualizarOrdenFiltro(ordenFiltro: OrdenFiltro){
        dataStore.edit {preferencias ->
            preferencias[PreferencesKeys.ORDEN_FILTRO] = ordenFiltro.name
        }
    }
    suspend fun  actualizarOcultarCompletadas(ocultarCompletadas: Boolean){
        dataStore.edit {preferencias ->
            preferencias[PreferencesKeys.OCULTAR_COMPLETADAS] = ocultarCompletadas
        }
    }
    private object PreferencesKeys {
        val ORDEN_FILTRO = preferencesKey<String>("orden_filtro")
        val OCULTAR_COMPLETADAS = preferencesKey<Boolean>("ocultar_completadas")
    }

}

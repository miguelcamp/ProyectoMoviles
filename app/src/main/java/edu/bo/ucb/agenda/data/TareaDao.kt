package edu.bo.ucb.agenda.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    fun getTareas(query: String, ordenFiltro: OrdenFiltro, ocultarCompletadas: Boolean): Flow<List<Tarea>> = when(ordenFiltro){
        OrdenFiltro.POR_FECHA -> getTareasOrdenadasPorFechaDeCreacion(query, ocultarCompletadas)
        OrdenFiltro.POR_NOMBRE -> getTareasOrdenadasPorNombre(query, ocultarCompletadas)
    }

    @Query("SELECT * FROM tabla_tareas WHERE (completada != :ocultarCompletadas OR completada = 0) AND nombre LIKE '%' || :searchQuery || '%' ORDER BY importante DESC, nombre")
    fun getTareasOrdenadasPorNombre(searchQuery: String, ocultarCompletadas: Boolean): Flow<List<Tarea>>

    @Query("SELECT * FROM tabla_tareas WHERE (completada != :ocultarCompletadas OR completada = 0) AND nombre LIKE '%' || :searchQuery || '%' ORDER BY importante DESC, creacion")
    fun getTareasOrdenadasPorFechaDeCreacion(searchQuery: String, ocultarCompletadas: Boolean): Flow<List<Tarea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarea: Tarea)

    @Update
    suspend fun update(tare: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)
}
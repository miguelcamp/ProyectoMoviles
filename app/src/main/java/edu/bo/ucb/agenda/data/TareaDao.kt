package edu.bo.ucb.agenda.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * FROM tabla_tareas")
    fun getTareas(): Flow<List<Tarea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarea: Tarea)

    @Update
    suspend fun update(tare: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)
}
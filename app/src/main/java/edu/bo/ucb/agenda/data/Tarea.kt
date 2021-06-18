package edu.bo.ucb.agenda.data


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "tabla_tareas")
@Parcelize
data class Tarea(
    val nombre: String,
    val importante: Boolean = false,
    val completada: Boolean = false,
    val creacion: Long = System.currentTimeMillis(),
    val fechaLimite: String ="01/01/2021",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val fechaCreacionFormateada: String
        get() = DateFormat.getDateTimeInstance().format(creacion)
}
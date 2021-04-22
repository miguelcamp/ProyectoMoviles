package edu.bo.ucb.agenda.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.bo.ucb.agenda.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Tarea::class], version = 1)
abstract class TareaDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao

    class Callback @Inject constructor(
        private val database: Provider<TareaDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().tareaDao()

            applicationScope.launch {
                dao.insert(Tarea("Lavar los platos"))
                dao.insert(Tarea("Lavar la ropa"))
                dao.insert(Tarea("Reunión con noel", importante = true))
                dao.insert(Tarea("Reunión con noel", completada = true))
                dao.insert(Tarea("Reparar la bici"))
            }
        }
    }
}
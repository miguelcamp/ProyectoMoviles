package edu.bo.ucb.agenda.di

import android.app.Application
import android.app.SharedElementCallback
import androidx.room.Room
import edu.bo.ucb.agenda.data.TareaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app:Application,
        callback: TareaDatabase.Callback
    ) = Room.databaseBuilder(app, TareaDatabase::class.java, "tarea_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()
    @Provides
    fun provideTareaDao(db:TareaDatabase) = db.tareaDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
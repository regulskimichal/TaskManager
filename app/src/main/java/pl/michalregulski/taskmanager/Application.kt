package pl.michalregulski.taskmanager

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.michalregulski.taskmanager.database.AppDatabase
import pl.michalregulski.taskmanager.model.TaskRepository
import pl.michalregulski.taskmanager.viewmodel.AddEditTaskViewModel
import pl.michalregulski.taskmanager.viewmodel.TaskListViewModel

class Application : Application() {

    private val module = module {
        single {
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app.db")
                .addCallback(AppDatabase.InitializationCallback(inject()))
                .fallbackToDestructiveMigration()
                .build()
        }
        single { TaskRepository(get<AppDatabase>().taskDao()) }
        viewModel { TaskListViewModel(get()) }
        viewModel { AddEditTaskViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(module)
        }
    }

}

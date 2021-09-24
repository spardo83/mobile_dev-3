package ar.edu.unlam.location

import android.app.Application
import ar.edu.unlam.location.repositories.DirectionsRepository
import ar.edu.unlam.location.repositories.impl.DirectionsRepositoryImpl
import ar.edu.unlam.location.retrofit.RetrofitApi
import ar.edu.unlam.location.ui.viewmodels.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MapsApp : Application() {
    val appModule = module {
        single { RetrofitApi() }
        single<DirectionsRepository> { DirectionsRepositoryImpl(get()) }
        viewModel { MapViewModel(get()) }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MapsApp)
            modules(appModule)
        }
    }

}
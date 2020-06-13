package pl.nataliana.githubfinder.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.nataliana.githubfinder.BuildConfig
import timber.log.Timber

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(
                listOf(
                    uiModule,
                    uiDetailModule,
                    githubFinderApiModule,
                    githubFinderApiClientModule
                )
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
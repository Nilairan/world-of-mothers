package com.madispace.worldofmothers

import android.app.Application
import com.madispace.di.navigationModule
import com.madispace.di.repositoryModule
import com.madispace.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/26/20
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                    listOf(
                            navigationModule,
                            useCasesModule,
                            repositoryModule
                    )
            )
        }
    }
}
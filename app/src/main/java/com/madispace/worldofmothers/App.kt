package com.madispace.worldofmothers

import android.app.Application
import com.madispace.di.*
import com.madispace.worldofmothers.ui.auth.SignInViewModel
import com.madispace.worldofmothers.ui.auth.SignUpViewModel
import com.madispace.worldofmothers.ui.catalog.CatalogViewModel
import com.madispace.worldofmothers.ui.createproduct.CreateProductViewModel
import com.madispace.worldofmothers.ui.favorites.FavoritesViewModel
import com.madispace.worldofmothers.ui.product.ProductViewModel
import com.madispace.worldofmothers.ui.profile.ProfileViewModel
import com.madispace.worldofmothers.ui.profile.change.ChangeProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    navigationModule,
                    useCasesModule,
                    repositoryModule,
                    apiModule,
                    databaseModule,
                    viewModelModule
                )
            )
        }
    }

    private val viewModelModule = module {
        viewModel { CatalogViewModel(get()) }
        viewModel { FavoritesViewModel(get(), get()) }
        viewModel { ProductViewModel(get(), get()) }
        viewModel { ProfileViewModel(get(), get()) }
        viewModel { SignInViewModel(get(), get()) }
        viewModel { SignUpViewModel(get(), get()) }
        viewModel { ChangeProfileViewModel(get(), get()) }
        viewModel { CreateProductViewModel(get(), get(), get()) }
    }
}
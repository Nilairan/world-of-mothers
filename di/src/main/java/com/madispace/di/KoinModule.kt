package com.madispace.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import com.madispace.di.routing.LocalCiceroneHolder
import com.madispace.domain.usecases.GetCatalogModelUseCase
import com.madispace.domain.usecases.GetCatalogModelUseCaseImpl
import com.madispace.domain.usecases.GetFavoritesProductUseCase
import com.madispace.domain.usecases.GetFavoritesProductUseCaseImpl
import org.koin.dsl.module

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/26/20
 */
val navigationModule = module {
    val cicerone: Cicerone<Router> = create()
    single { cicerone.router }
    single { cicerone.getNavigatorHolder() }
    single { LocalCiceroneHolder() }
}

val useCasesModule = module {
    single<GetCatalogModelUseCase> { GetCatalogModelUseCaseImpl() }
    single<GetFavoritesProductUseCase> { GetFavoritesProductUseCaseImpl() }
}
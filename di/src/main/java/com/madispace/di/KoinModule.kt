package com.madispace.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import com.madispace.core.network.ApiFactory
import com.madispace.core.network.product.ProductDataSource
import com.madispace.core.network.product.ProductDataSourceImpl
import com.madispace.core.repository.ProductListRepositoryImpl
import com.madispace.core.repository.UserRepositoryImpl
import com.madispace.di.routing.LocalCiceroneHolder
import com.madispace.domain.repository.ProductListRepository
import com.madispace.domain.repository.UserRepository
import com.madispace.domain.usecases.GetFavoritesProductUseCase
import com.madispace.domain.usecases.GetFavoritesProductUseCaseImpl
import com.madispace.domain.usecases.auth.AuthUseCase
import com.madispace.domain.usecases.auth.AuthUseCaseImpl
import com.madispace.domain.usecases.auth.ValidUseCase
import com.madispace.domain.usecases.auth.ValidUseCaseImpl
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCase
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCaseImpl
import com.madispace.domain.usecases.profile.*
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
    single<GetCatalogModelUseCase> { GetCatalogModelUseCaseImpl(get()) }
    single<GetFavoritesProductUseCase> { GetFavoritesProductUseCaseImpl() }
    single<IsAuthorizedUserUseCase> { IsAuthorizedUserUseCaseImpl(get()) }
    single<RegisterUserUseCase> { RegisterUserUseCaseImpl(get()) }
    single<GetUserProductUseCase> { GetUserProductUseCaseImpl() }
    single<ValidUseCase> { ValidUseCaseImpl() }
    single<AuthUseCase> { AuthUseCaseImpl(get()) }
}

val apiModule = module {
    single { ApiFactory().getApi() }
    single<ProductDataSource> { ProductDataSourceImpl(get()) }
}

val repositoryModule = module {
    single<ProductListRepository> { ProductListRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl() }
}
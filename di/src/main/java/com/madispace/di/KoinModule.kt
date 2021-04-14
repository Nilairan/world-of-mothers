package com.madispace.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import com.madispace.core.database.AppDatabase
import com.madispace.core.network.common.ApiFactory
import com.madispace.core.network.product.ProductDataSource
import com.madispace.core.network.product.ProductDataSourceImpl
import com.madispace.core.network.user.UserDataSource
import com.madispace.core.network.user.UserDataSourceImpl
import com.madispace.core.repository.ProductRepositoryImpl
import com.madispace.core.repository.UserRepositoryImpl
import com.madispace.di.routing.LocalCiceroneHolder
import com.madispace.domain.repository.ProductRepository
import com.madispace.domain.repository.UserRepository
import com.madispace.domain.usecases.auth.*
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCase
import com.madispace.domain.usecases.catalog.GetCatalogModelUseCaseImpl
import com.madispace.domain.usecases.product.*
import com.madispace.domain.usecases.profile.*
import org.koin.dsl.module

val navigationModule = module {
    val cicerone: Cicerone<Router> = create()
    single { cicerone.router }
    single { cicerone.getNavigatorHolder() }
    single { LocalCiceroneHolder() }
}

val useCasesModule = module {
    single<GetCatalogModelUseCase> { GetCatalogModelUseCaseImpl(get()) }
    single<GetFavoritesProductUseCase> { GetFavoritesProductUseCaseImpl(get()) }
    single<IsAuthorizedUserUseCase> { IsAuthorizedUserUseCaseImpl(get()) }
    single<RegisterUserUseCase> { RegisterUserUseCaseImpl(get()) }
    single<GetUserProductUseCase> { GetUserProductUseCaseImpl() }
    single<ValidUseCase> { ValidUseCaseImpl() }
    single<AuthUseCase> { AuthUseCaseImpl(get(), get()) }
    single<GetProductModelUseCase> { GetProductModelUseCaseImpl(get()) }
    single<FavoriteProductUseCase> { FavoriteProductUseCaseImpl(get()) }
    single<EncodeUserDataUseCase> { EncodeUserDataUseCaseImpl() }
}

val apiModule = module {
    single { ApiFactory().getApi() }
    single<ProductDataSource> { ProductDataSourceImpl(get(), get()) }
    single<UserDataSource> { UserDataSourceImpl(get(), get()) }
}

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().productDao }
    single { get<AppDatabase>().userTokenDao }
}
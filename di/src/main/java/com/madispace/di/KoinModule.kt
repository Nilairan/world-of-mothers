package com.madispace.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import com.madispace.di.routing.LocalCiceroneHolder
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
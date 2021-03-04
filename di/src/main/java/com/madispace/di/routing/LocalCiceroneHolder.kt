package com.madispace.di.routing

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import java.util.*

class LocalCiceroneHolder {
    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> =
            containers.getOrPut(containerTag) {
                Cicerone.create()
            }
}
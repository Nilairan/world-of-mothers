package com.madispace.worldofmothers.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.madispace.worldofmothers.ui.catalog.CatalogFragment
import com.madispace.worldofmothers.ui.common.TabContainerFragment
import com.madispace.worldofmothers.ui.favorites.FavoritesFragment
import com.madispace.worldofmothers.ui.profile.ProfileFragment
import com.madispace.worldofmothers.ui.profile.auth.SignInFragment
import com.madispace.worldofmothers.ui.profile.auth.SignUpFragment

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/26/20
 */
object Screens {

    fun TabContainer(tabName: String) =
            FragmentScreen(key = tabName) { TabContainerFragment.newInstance(tabName) }

    fun CatalogScreen() = FragmentScreen { CatalogFragment.newInstance() }

    fun FavoritesScreen() = FragmentScreen { FavoritesFragment.newInstance() }

    fun ProfileScreen() = FragmentScreen { ProfileFragment.newInstance() }

    fun SignInScreen() = FragmentScreen { SignInFragment.newInstance() }

    fun SignUpScreen() = FragmentScreen { SignUpFragment.newInstance() }
}
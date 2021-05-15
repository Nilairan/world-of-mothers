package com.madispace.worldofmothers.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.madispace.worldofmothers.ui.auth.SignInFragment
import com.madispace.worldofmothers.ui.auth.SignUpFragment
import com.madispace.worldofmothers.ui.catalog.CatalogFragment
import com.madispace.worldofmothers.ui.common.TabContainerFragment
import com.madispace.worldofmothers.ui.favorites.FavoritesFragment
import com.madispace.worldofmothers.ui.product.ProductFragment
import com.madispace.worldofmothers.ui.profile.ProfileFragment
import com.madispace.worldofmothers.ui.profile.change.ChangeProfileFragment
import com.madispace.worldofmothers.ui.profile.product.NewProductFragment

object Screens {

    fun TabContainer(tabName: String) =
            FragmentScreen(key = tabName) { TabContainerFragment.newInstance(tabName) }

    fun CatalogScreen() = FragmentScreen { CatalogFragment.newInstance() }

    fun FavoritesScreen() = FragmentScreen { FavoritesFragment.newInstance() }

    fun ProfileScreen() = FragmentScreen { ProfileFragment.newInstance() }

    fun SignInScreen() = FragmentScreen { SignInFragment.newInstance() }

    fun SignUpScreen() = FragmentScreen { SignUpFragment.newInstance() }

    fun ChangeProfileScreen() = FragmentScreen { ChangeProfileFragment.newInstance() }

    fun NewProductScreen() = FragmentScreen { NewProductFragment.newInstance() }

    fun ProductScreen(productId: Int) = FragmentScreen { ProductFragment.newInstance(productId) }
}
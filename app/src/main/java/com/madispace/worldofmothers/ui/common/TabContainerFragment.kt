package com.madispace.worldofmothers.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.madispace.di.routing.LocalCiceroneHolder
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.BackButtonListener
import com.madispace.worldofmothers.databinding.FragmentTabContainerBinding
import com.madispace.worldofmothers.routing.RouterProvider
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.CatalogFragment
import com.madispace.worldofmothers.ui.favorites.FavoritesFragment
import com.madispace.worldofmothers.ui.profile.ProfileFragment
import org.koin.android.ext.android.inject
import kotlin.reflect.jvm.jvmName

class TabContainerFragment : Fragment(), RouterProvider, BackButtonListener {

    private lateinit var binding: FragmentTabContainerBinding
    private val ciceroneHolder: LocalCiceroneHolder by inject()
    private val containerName: String
        get() = arguments!!.getString(TAB_NAME)!!
    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerName)
    private val navigator: Navigator by lazy {
        AppNavigator(activity!!, R.id.tabContainer, childFragmentManager)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.tabContainer) == null) {
            val screen: Screen = when (containerName) {
                CatalogFragment::class.jvmName -> Screens.CatalogScreen()
                FavoritesFragment::class.jvmName -> Screens.FavoritesScreen()
                ProfileFragment::class.jvmName -> Screens.ProfileScreen()
                else -> throw TypeCastException()
            }
            cicerone.router.replaceScreen(screen)
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override val router: Router
        get() = cicerone.router

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.tabContainer)
        return if (fragment != null && fragment is BackButtonListener
                && (fragment as BackButtonListener).onBackPressed()) {
            true
        } else {
            (activity as RouterProvider?)!!.router.exit()
            false
        }
    }

    companion object {

        private const val TAB_NAME = "TAB_NAME"

        fun newInstance(tabName: String) = TabContainerFragment().apply {
            arguments = Bundle().apply {
                putString(TAB_NAME, tabName)
            }
        }
    }
}
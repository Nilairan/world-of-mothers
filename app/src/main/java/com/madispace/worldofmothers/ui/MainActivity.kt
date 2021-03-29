package com.madispace.worldofmothers.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.BackButtonListener
import com.madispace.worldofmothers.databinding.ActivityMainBinding
import com.madispace.worldofmothers.routing.RouterProvider
import com.madispace.worldofmothers.routing.Screens.TabContainer
import com.madispace.worldofmothers.ui.catalog.CatalogFragment
import com.madispace.worldofmothers.ui.favorites.FavoritesFragment
import com.madispace.worldofmothers.ui.profile.ProfileFragment
import org.koin.android.ext.android.inject
import kotlin.reflect.jvm.jvmName

class MainActivity : AppCompatActivity(R.layout.activity_main),
    RouterProvider, BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding: ActivityMainBinding by viewBinding()
    override val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.catalog
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.catalog -> {
                selectTab(CatalogFragment::class.jvmName)
                true
            }
            R.id.favorites -> {
                selectTab(FavoritesFragment::class.jvmName)
                true
            }
            R.id.profile -> {
                selectTab(ProfileFragment::class.jvmName)
                true
            }
            else -> false
        }
    }

    private fun selectTab(tab: String) {
        val fm = supportFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                currentFragment = f
                break
            }
        }
        val newFragment = fm.findFragmentByTag(tab)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fm.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                    R.id.navHostFragment,
                    TabContainer(tab).createFragment(fm.fragmentFactory), tab
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        var fragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                fragment = f
                break
            }
        }
        if (fragment != null && fragment is BackButtonListener
                && (fragment as BackButtonListener).onBackPressed()) {
            return
        } else {
            finish()
        }
    }
}
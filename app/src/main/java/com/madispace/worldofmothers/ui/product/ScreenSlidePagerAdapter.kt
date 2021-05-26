package com.madispace.worldofmothers.ui.product

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(
    fa: FragmentActivity,
    private val items: List<String>
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(items[position])
    }
}
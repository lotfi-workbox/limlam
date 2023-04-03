@file:Suppress("DEPRECATION")

package com.saeedlotfi.limlam.userInterface.activities.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.fragments.*
import com.saeedlotfi.limlam.userInterface.fragments.playlists.PlayListsFragment

class MainTabAdapter private constructor(private val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments: MutableList<Fragment> = mutableListOf(
        TracksFragment(),
        PlayListsFragment(),
        FoldersFragment(),
        AlbumsFragment(),
        ArtistsFragment(),
        GenresFragment()
    )

    fun clear() {
        val transaction: FragmentTransaction = fm.beginTransaction()
        for (fragment in fragments) {
            transaction.remove(fragment)
        }
        fragments.clear()
        transaction.commitNowAllowingStateLoss()
    }

    override fun getCount(): Int = 6

    override fun getItem(position: Int): Fragment = fragments[position]

    companion object {
        fun configWithTabLayout(
            viewPager: ViewPager, tabLayout: TabLayout, fm: FragmentManager
        ) = with(viewPager) {
            viewPager.adapter = MainTabAdapter(fm)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(0)?.text = context.getString(R.string.tracks)
            tabLayout.getTabAt(1)?.text = context.getString(R.string.playlists)
            tabLayout.getTabAt(2)?.text = context.getString(R.string.folders)
            tabLayout.getTabAt(3)?.text = context.getString(R.string.albums)
            tabLayout.getTabAt(4)?.text = context.getString(R.string.artists)
            tabLayout.getTabAt(5)?.text = context.getString(R.string.genres)
        }
    }

}
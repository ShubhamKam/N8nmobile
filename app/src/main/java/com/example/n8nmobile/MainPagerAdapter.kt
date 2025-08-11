package com.example.n8nmobile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment =
        if (position == 0) N8nWebFragment() else ChatFragment()
}

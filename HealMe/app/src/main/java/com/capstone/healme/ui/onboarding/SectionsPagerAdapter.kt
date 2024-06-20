package com.capstone.healme.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = OnboardingItemFragment()
        fragment.arguments = Bundle().apply {
            putInt(TAB_STATE, position)
        }
        return fragment
    }

    companion object {
        const val TAB_STATE = "tab_index"
    }
}
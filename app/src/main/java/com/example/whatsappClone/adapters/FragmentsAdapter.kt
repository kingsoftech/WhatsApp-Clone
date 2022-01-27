package com.example.whatsappClone.adapters


import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentManager

import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappClone.fragments.CallsFragment
import com.example.whatsappClone.fragments.ChatFragment
import com.example.whatsappClone.fragments.StatusFragment

class FragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
     var titles: ArrayList<String> = ArrayList()


    override fun getItemCount(): Int {
        return  3
    }

    override fun createFragment(position: Int): Fragment {
        titles.add("Chats")
        titles.add("Status")
        titles.add("Calls")
        return when(position){
            0->{
                ChatFragment()
                }
            1->{
                StatusFragment()
            }
            2->{
                CallsFragment()
            }
            else->{
                Fragment()
            }
        }

    }
}
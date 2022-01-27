package com.example.whatsappClone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappClone.Firebase.RealTimeDatabase
import com.example.whatsappClone.adapters.ChatFragmentAdapter
import com.example.whatsappClone.databinding.FragmentChatBinding
import com.example.whatsappClone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : Fragment() {
   private var lists:ArrayList<User> = ArrayList()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var fragmentChatBinding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        fragmentChatBinding= FragmentChatBinding.inflate(inflater, container, false)
        fragmentChatBinding.rvChat.layoutManager =LinearLayoutManager(requireActivity())
        val userAdapter = ChatFragmentAdapter(this@ChatFragment, lists)
        fragmentChatBinding.rvChat.adapter = userAdapter
        RealTimeDatabase().loadUsers(lists, userAdapter)
        // Inflate the layout for this fragment
        return fragmentChatBinding.root
    }

}
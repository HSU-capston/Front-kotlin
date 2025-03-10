package com.example.capston_spotyup.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_spotyup.R

class FriendsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mypage_friend, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val friendsList = listOf("친구 1", "친구 2", "친구 3")
        val adapter = FriendsAdapter(friendsList)
        recyclerView.adapter = adapter

        return view
    }
}

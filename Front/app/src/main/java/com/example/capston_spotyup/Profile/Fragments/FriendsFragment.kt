package com.example.capston_spotyup.Profile.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_spotyup.Profile.Domain.FriendViewModel
import com.example.capston_spotyup.Profile.Domain.FriendsAdapter
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Profile.Domain.FriendViewModelFactory
import com.example.capston_spotyup.R

class FriendsFragment : Fragment() {

    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var friendViewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_friend, container, false)

        // RecyclerView 초기화
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView)
        friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // FriendViewModel 초기화
        val friendApi = RetrofitClient.friendApi
        friendViewModel = ViewModelProvider(this, FriendViewModelFactory(friendApi)).get(FriendViewModel::class.java)

        // 친구 목록 불러오기
        friendViewModel.getFriends()

        // LiveData 관찰하여 RecyclerView에 데이터 반영
        friendViewModel.friendsList.observe(viewLifecycleOwner, Observer { friends ->
            friendsAdapter = FriendsAdapter(friends)
            friendsRecyclerView.adapter = friendsAdapter
        })

        return view
    }
}

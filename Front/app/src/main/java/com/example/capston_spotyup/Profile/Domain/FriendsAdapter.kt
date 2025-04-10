package com.example.capston_spotyup.Profile.Domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capston_spotyup.Profile.DTO.Response.Friend
import com.example.capston_spotyup.R

class FriendsAdapter(private val friendsList: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friendProfile: ImageView = view.findViewById(R.id.friendProfile)
        val friendName: TextView = view.findViewById(R.id.friendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mypage_friend, parent, false)  // 아이템 레이아웃 수정
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendsList[position]
        holder.friendName.text = friend.name

        // 친구 프로필 이미지 로드 (Glide 사용)
        Glide.with(holder.itemView.context)
            .load(friend.profileImageUrl)
            .placeholder(R.drawable.ic_profile_gray) // 기본 회색 프로필 이미지
            .circleCrop()
            .into(holder.friendProfile)
    }

    override fun getItemCount(): Int = friendsList.size
}